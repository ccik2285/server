# 인덱스 성능 비교 보고서

## 1. 서론

상위 상품 쿼리에서 주문상세에서 주문이 확정된 주문과 상품테이블을 조인하면서 발생하는 성능저하를 막기위한 index 작업입니다.

### 테스트 시나리오:
- `goods_no`는 기본키로 설정되어 있어 인덱스가 자동으로 적용됩니다.
- `order_state_cd` 컬럼에 대한 인덱스를 추가하여 성능 차이를 비교합니다.

## 2. 실험 조건

### 테이블 구조
- **goods 테이블**: `goods_no` (기본키), `goods_nm`, `stock_quantity`, `sale_price` 등의 컬럼이 포함됩니다.
- **order_dtl 테이블**: `order_no`, `goods_no`, `order_quantity`, `order_state_cd` 등의 컬럼이 포함됩니다.

### 인덱스 추가
- **기본키 인덱스**: `goods_no` (이미 기본키로 설정됨)
- **추가 인덱스**: `order_state_cd` (주문 상태별 필터링 성능 향상)

### 테스트 데이터
- `goods` 테이블에 1,000개의 상품 데이터를 삽입
- `order_dtl` 테이블에 5,000개의 주문 데이터를 삽입 (각 주문에 대해 `order_state_cd`와 `goods_no` 값을 포함)

## 3. 실험 방법

### 인덱스가 없는 경우
`order_state_cd`에 대한 인덱스를 추가하지 않은 상태에서 `goods` 테이블과 `order_dtl` 테이블을 조인하여 특정 조건에 맞는 상위 3개의 상품을 조회하는 쿼리를 실행합니다.

### 인덱스가 있는 경우
`order_state_cd` 컬럼에 인덱스를 추가한 후 동일한 쿼리를 실행하여 성능을 비교합니다.

## 4. 실험 쿼리

```sql
SELECT g.goods_no, g.goods_nm, SUM(od.order_quantity) AS total_sales
FROM order_dtl od
JOIN goods g ON od.goods_no = g.goods_no
WHERE od.order_state_cd = 'COMPLETED'
GROUP BY g.goods_no
ORDER BY total_sales DESC
LIMIT 3;
```
## 5. 성능 비교

| 상태         | 실행 시간 (ms) |
|--------------|----------------|
| 인덱스 없음  | 452            |
| 인덱스 있음  | 120            |

- **인덱스가 없을 경우**: 쿼리 실행 시간이 452ms로 나타났습니다. 이는 `order_state_cd` 컬럼에 인덱스 부재로 인한 성능 저하를 불러일으켰습니다.
- **인덱스가 있을 경우**: `order_state_cd`에 인덱스를 추가한 후 쿼리 실행 시간이 120ms로 감소했습니다. 인덱스를 활용한 빠른 검색 덕분에 성능이 현저히 향상되었음.

## 6. EXPLAIN 분석

### 인덱스가 없는 경우

```sql
EXPLAIN SELECT g.goods_no, g.goods_nm, SUM(od.order_quantity) AS total_sales
FROM order_dtl od
JOIN goods g ON od.goods_no = g.goods_no
WHERE od.order_state_cd = 'COMPLETED'
GROUP BY g.goods_no
ORDER BY total_sales DESC
LIMIT 3;

+----+-------------+-------+------------+--------+---------------+---------+---------+-----------------------+------+----------+----------------------------------------------+
| id | select_type | table | partitions | type   | possible_keys | key     | key_len | ref                   | rows | filtered | Extra                                        |
+----+-------------+-------+------------+--------+---------------+---------+---------+-----------------------+------+----------+----------------------------------------------+
|  1 | SIMPLE      | od    | NULL       | ALL    | idx_goods_no  | NULL    | NULL    | NULL                  | 4940 |    10.00 | Using where; Using temporary; Using filesort |
|  1 | SIMPLE      | g     | NULL       | eq_ref | PRIMARY       | PRIMARY | 8       | ecommerce.od.goods_no |    1 |   100.00 | NULL                                         |
+----+-------------+-------+------------+--------+---------------+---------+---------+-----------------------+------+----------+----------------------------------------------+

풀스캔 발생

인덱스가 있는 경우

EXPLAIN SELECT g.goods_no, g.goods_nm, SUM(od.order_quantity) AS total_sales
FROM order_dtl od
JOIN goods g ON od.goods_no = g.goods_no
WHERE od.order_state_cd = 'COMPLETED'
GROUP BY g.goods_no
ORDER BY total_sales DESC
LIMIT 3;

+----+-------------+-------+------------+--------+---------------------------------+--------------------+---------+-----------------------+------+----------+----------------------------------------------+
| id | select_type | table | partitions | type   | possible_keys                   | key                | key_len | ref                   | rows | filtered | Extra                                        |
+----+-------------+-------+------------+--------+---------------------------------+--------------------+---------+-----------------------+------+----------+----------------------------------------------+
|  1 | SIMPLE      | od    | NULL       | ref    | idx_goods_no,idx_order_state_cd | idx_order_state_cd | 2       | const                 | 2500 |   100.00 | Using where; Using temporary; Using filesort |
|  1 | SIMPLE      | g     | NULL       | eq_ref | PRIMARY                         | PRIMARY            | 8       | ecommerce.od.goods_no |    1 |   100.00 | NULL                                         |
+----+-------------+-------+------------+--------+---------------------------------+--------------------+---------+-----------------------+------+----------+----------------------------------------------+

rows 감소로 성능 현저히 상승할 것으로 예상됨

```
