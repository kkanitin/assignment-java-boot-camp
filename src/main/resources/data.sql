--SET DEFAULT_LOCK_TIMEOUT 2000;

INSERT INTO "PUBLIC"."USER_INFO"
VALUES
(1, 'tom', 'tom', '1RM1L4ySu1WZ/M9d5de3vA=='),
(2, 'somchai', 'somchai', 'yrn4TyjwUqJSREfP/X5JNQ=='),
(3, 'alex', 'alex', 'WplLkrJhDRnQRennyPQ+Sw=='),
(4, 'tony', 'tony', '086Kf9qDzKujM8FF2QEUlA==');

COMMIT;

ALTER SEQUENCE USERS_SEQ RESTART WITH 5;

INSERT INTO "PUBLIC"."USER_CARD"
VALUES
(1,'123456','VISA','123','01','26',1,1),
(2,'234567','MASTERCARD','456','02','26',2,1),
(3,'345678','MASTERCARD','789','03','26',1,2),
(4,'456789','VISA','101','04','26',1,3),
(5,'567891','VISA','121','05','26',1,4);

COMMIT;

ALTER SEQUENCE USERCARD_SEQ RESTART WITH 6;

INSERT INTO "PUBLIC"."ADDRESS"
VALUES
(1,'อาคาร 1','1','11/22','เขต 1','แขวง 1','10200','กรุงเทพ','ประเสริฐมนูกิจ','2 แยก2-3','หมูบ้าน หนึ่ง ทดสอบ',1,1),
(2,'อาคาร 2','2','22/33','เขต 2','แขวง 2','10300','กรุงเทพ','ประเสริฐมนูกิจ','3 แยก1-2','หมูบ้าน สอง ทดสอบ',2,1),
(3,'อาคาร 3','3','32/23','เขต 3','แขวง 3','10400','กรุงเทพ','ประเสริฐมนูกิจ','4 แยก2-1','หมูบ้าน สาม ทดสอบ',3,1),
(4,'อาคาร 4','4','23/32','เขต 4','แขวง 4','10500','กรุงเทพ','ประเสริฐมนูกิจ','5 แยก4-2','หมูบ้าน สี่ ทดสอบ',4,1);

COMMIT;

ALTER SEQUENCE ADDRESS_SEQ RESTART WITH 5;

INSERT INTO "PUBLIC"."PRODUCT"
VALUES
(1,'รายละเอียด สินค้า ทดสอบ 1','สินค้า 1',100,20),
(2,'รายละเอียด สินค้า ทดสอบ 2','สินค้า 2',200,20),
(3,'รายละเอียด สินค้า ทดสอบ 3','สินค้า 3',300,20),
(4,'รายละเอียด สินค้า ทดสอบ 4','สินค้า 4',400,20),
(5,'รายละเอียด สินค้า ทดสอบ 5','สินค้า 5',500,20);

COMMIT;

ALTER SEQUENCE PRODUCT_SEQ RESTART WITH 6;

INSERT INTO "PUBLIC"."BASKET"
VALUES
(1,2,1,1),
(2,5,2,1);

ALTER SEQUENCE BASKET_SEQ RESTART WITH 3;

COMMIT;



   
