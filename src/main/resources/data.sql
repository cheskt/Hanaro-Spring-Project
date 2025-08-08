insert into Member(email, password, role, createdAt) value('hanaro', '12345678', 'ADMIN', now());

select * from Member;

insert into Item(itemName, price, stock) values ('1번 상품', 10000, 1),
                                                ('2번 상품', 20000, 2),
                                                ('3번 상품', 30000, 2)
