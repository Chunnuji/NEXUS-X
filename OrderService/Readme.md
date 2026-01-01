#
Order Schema

{
int orderID (auto increment) ,
int productId(foreign key in product table),
int quantity,
int userid,
int price
}

dto-

orderID
productID
quantity


apis-
/placeOrder
/getOrder
/getOrderByID/{id}
/cancelOrder


orders
{
order_id        BIGINT AUTO_INCREMENT PRIMARY KEY,
product_id      BIGINT NOT NULL,
quantity        INT NOT NULL,
user_id         VARCHAR(50) NOT NULL,
unit_price      DECIMAL(10,2) NOT NULL,
total_price     DECIMAL(10,2) NOT NULL,
status          VARCHAR(20) NOT NULL,
created_at      TIMESTAMP NOT NULL,
updated_at      TIMESTAMP NOT NULL
}
