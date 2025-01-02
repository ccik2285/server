# Project E-COMMERCE

## 프로젝트 구조
```
├── main
│   ├── java
│   │   └── kr
│   │       └── hhplus
│   │           └── be
│   │               └── server
│   │                   ├── ServerApplication.java
│   │                   ├── admin
│   │                   │   └── domain
│   │                   │       └── view
│   │                   │           └── usecase
│   │                   ├── api
│   │                   │   └── domain
│   │                   │       ├── controller
│   │                   │       │   └── GoodsController.java
│   │                   │       ├── dto
│   │                   │       │   ├── request
│   │                   │       │   └── response
│   │                   │       └── usecase
│   │                   ├── common
│   │                   │   ├── AssetStateCd.java
│   │                   │   ├── AssetTypeCd.java
│   │                   │   ├── BaseEntity.java
│   │                   │   ├── GoodsStateCd.java
│   │                   │   ├── OrderStateCd.java
│   │                   │   └── PayStateCd.java
│   │                   ├── config
│   │                   │   └── jpa
│   │                   │       └── JpaConfig.java
│   │                   └── domain
│   │                       ├── coupon
│   │                       │   └── models
│   │                       │       └── Coupon.java
│   │                       ├── goods
│   │                       │   └── models
│   │                       │       ├── Goods.java
│   │                       │       └── SummaryGoods.java
│   │                       ├── member
│   │                       │   └── models
│   │                       │       ├── Member.java
│   │                       │       └── MemberAsset.java
│   │                       ├── order
│   │                       │   └── models
│   │                       │       └── Order.java
│   │                       └── pay
│   │                           └── models
│   │                               └── PayInfo.java
│   └── resources
│       └── application.yml
└── test
    └── java
        └── kr
            └── hhplus
                └── be
                    └── server
                        ├── ServerApplicationTests.java
                        └── TestcontainersConfiguration.java

```
