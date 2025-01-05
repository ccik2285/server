
# API 명세서

## 1. 잔액 충전 API

### **POST /api/member/asset/charge**
- **기능**: 회원의 자산에 금액을 충전합니다.
- **요청**
  ```json
  {
    "mbr_no": 12345,
    "asset_type_cd": "POINT", 
    "amount": 1000  // 충전할 금액
  }
  ```
- **응답**
  - **성공**:
    ```json
    {
      "status": "success",
      "message": "잔액이 충전되었습니다.",
      "new_balance": 1500  // 충전 후 잔액
    }
    ```
  - **실패 (잔액 부족 등)**:
    ```json
    {
      "status": "failure",
      "message": "충전 실패. 자산 유형을 확인하세요."
    }
    ```

### **GET /api/member/asset/{mbr_no}/{asset_type_cd}**
- **기능**: 회원의 자산 잔액을 조회합니다.
- **요청**
  ```json
  {
    "mbr_no": 12345,
    "asset_type_cd": "POINT" 
  }
  ```
- **응답**
  - **성공**:
    ```json
    {
      "status": "success",
      "balance": 1500 
    }
    ```
  - **실패 (유효하지 않은 자산 유형 등)**:
    ```json
    {
      "status": "failure",
      "message": "유효한 자산 유형을 입력해주세요."
    }
    ```

---

## 2. 상품 조회 API

### **GET /api/goods/{goods_no}**
- **기능**: 상품 목록을 조회합니다.
- **요청**
  ```json
  {
    "goods_no" : 101
  }
  ```
- **응답**
  - **성공**:
    ```json
    {
      "status": "success",
      "goods":
        {
          "goods_no": 101,
          "goods_nm": "상품 1",
          "sale_price": 10000,
          "stock_quantity": 50
        }
    }
    ```
  - **실패 (잘못된 페이지 번호 등)**:
    ```json
    {
      "status": "failure",
      "message": "잘못된 페이지 번호입니다."
    }
    ```

---

## 3. 선착순 쿠폰 기능 API

### **POST /api/coupon/issue**
- **기능**: 선착순 쿠폰을 발급합니다.
- **요청**
  ```json
  {
    "mbr_no": 12345,
    "coupon_no":12345,
    "asset_type_cd": "COUPON"
  }
  ```
- **응답**
  - **성공 (쿠폰 발행됨)**:
    ```json
    {
      "status": "success",
      "message": "쿠폰이 발행되었습니다."
    }
    ```
  - **실패 (쿠폰 소진 등)**:
    ```json
    {
      "status": "failure",
      "message": "쿠폰이 소진되었습니다."
    }
    ```

---

## 4. 주문/결제 API

### **POST /api/order**
- **기능**: 상품을 주문서를 생성합니다.
- **요청**
  ```json
  {
    "mbr_no": 12345,
    "goods_no": 101,
    "quantity": 2
  }
  ```
- **응답**
  - **성공**:
    ```json
    {
      "status": "success",
      "message": "주문서 만들기 성공"
    }
    ```
  - **실패 (재고 부족 등)**:
    ```json
    {
      "status": "failure",
      "message": "주문 실패. 재고가 부족합니다."
    }
    ```

### **POST /api/pay**
- **기능**: 주문에 대한 결제를 완료합니다.
- **요청**
  ```json
  {
    "order_no": 123456,
    "payment_amount": 20000,
    "asset_type_cd": "COUPON"  // 결제 자산 유형
  }
  ```
- **응답**
  - **성공**:
    ```json
    {
      "status": "success",
      "message": "결제가 완료되었습니다.",
      "pay_no": 789012
    }
    ```
  - **실패 (잔액 부족 등)**:
    ```json
    {
      "status": "failure",
      "message": "결제 실패. 잔액이 부족합니다."
    }
    ```

---

## 5. 상위 상품 조회 API

### **GET /api/goods/top**
- **기능**: 상위 판매 상품을 조회합니다.
- **요청**
  ```json
  {
    
  }
  ```
- **응답**
  - **성공**:
    ```json
    {
      "status": "success",
      "top_goods": [
        {
          "goods_no": 101,
          "goods_nm": "상품 1",
          "sale_price": 10000,
          "sales_count": 200
        },
        {
          "goods_no": 102,
          "goods_nm": "상품 2",
          "sale_price": 20000,
          "sales_count": 150
        }
      ]
    }
    ```
  - **실패 (잘못된 파라미터 등)**:
    ```json
    {
      "status": "failure",
      "message": "잘못된 요청입니다."
    }
    ```

---

## **API 명세 요약**
- **잔액 충전/조회**: `POST /api/member/asset/charge`, `GET /api/member/asset/{mbr_no}/{asset_type_cd}`
- **상품 조회**: `GET /api/goods/{goods_no}`
- **선착순 쿠폰 발급**: `POST /api/coupon/issue`
- **주문/결제**: `POST /api/order`, `POST /api/pay`
- **상위 상품 조회**: `GET /api/goods/top`

