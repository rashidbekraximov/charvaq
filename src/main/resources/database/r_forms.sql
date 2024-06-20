insert into r_forms(id, form_number, name, order_number, status, created_by, created_on,
                    modified_by, modified_on, href, parent_id)
VALUES (100, 100, row ('Продажа', 'Сотиш', 'Sotish'), 100, 'ACTIVE', null, now(), null, null, null, null);

insert into r_forms(id, form_number, name, order_number,  status, created_by, created_on,
                    modified_by, modified_on, href, parent_id)
VALUES (1, 1,
        row ('Продажа', 'Сотиш', 'Sotish'),
        1, 'ACTIVE', null, now(), null, null, '/purchase/add', 100);

insert into r_forms(id, form_number, name, order_number,  status, created_by, created_on,
                    modified_by, modified_on, href, parent_id)
VALUES (2, 2,
        row ('Погашение долга','Қарздорликни сўндириш','Qarzdorlikni so''ndirish'),
        2, 'ACTIVE', null, now(), null, null, '/references/orders', 100);

insert into r_forms(id, form_number, name, order_number,  status, created_by, created_on,
                    modified_by, modified_on, href, parent_id)
VALUES (3, 3,
        row ('Заказы список', 'Буюртмалар рўйхати', 'Buyurtmalar ro''yxati'),
        3, 'ACTIVE', null, now(), null, null, '/references/orders', 100);

insert into r_forms(id, form_number, name, order_number,  status, created_by, created_on,
                    modified_by, modified_on, href, parent_id)
VALUES (4, 4,
        row ('Цены на продукцию', 'Маҳсулот нархлари', 'Mahsulot narxlari'),
        4, 'ACTIVE', null, now(), null, null, '/references/products-price', 100);

insert into r_forms(id, form_number, name, order_number,  status, created_by, created_on,
                    modified_by, modified_on, href, parent_id)
VALUES (5, 5,
        row ('Остаток продукта', 'Маҳсулот қолдиғи', 'Mahsulot qoldig''i'),
        5, 'ACTIVE', null, now(), null, null, '/references/products-remainder', 100);

insert into r_forms(id, form_number, name, order_number,  status, created_by, created_on,
                    modified_by, modified_on, href, parent_id)
VALUES (6, 6,
        row ('Список продаж', 'Сотувлар рўйхати', 'Sotuvlar ro''yxati'),
        6, 'ACTIVE', null, now(), null, null, '/purchase/list', 100);

insert into r_forms(id, form_number, name, order_number,  status, created_by, created_on,
                    modified_by, modified_on, href, parent_id)
VALUES (7, 7,
        row ('Ежедневная стоимость', 'Кунлик харажат', 'Kunlik xarajat'),
        7, 'ACTIVE', null, now(), null, null, '/purchase/daily-cost', 100);


insert into r_forms(id, form_number, name, order_number, status, created_by, created_on,
                    modified_by, modified_on, href, parent_id)
VALUES (101, 101, row ('Логистика', 'Логистика', 'Logistika'), 101, 'ACTIVE', null, now(), null, null, null,
        null);

insert into r_forms(id, form_number, name, order_number,  status, created_by, created_on,
                    modified_by, modified_on, href, parent_id)
VALUES (102, 102,
        row ('Tехники', 'Техникалар', 'Texnikalar'),
        102, 'ACTIVE', null, now(), null, null, '/references/techniques', 101);

insert into r_forms(id, form_number, name, order_number,  status, created_by, created_on,
                    modified_by, modified_on, href, parent_id)
VALUES (103, 103,
        row ('Дробилка и завод', 'Дробилка ва завод', 'Drobilka va zavod'),
        103, 'ACTIVE', null, now(), null, null, '/logistic/products', 101);


insert into r_forms(id, form_number, name, order_number,  status, created_by, created_on,
                    modified_by, modified_on, href, parent_id)
VALUES (104, 104,
        row ('Mahsulot kirimi', 'Mahsulot kirimi', 'Mahsulot kirimi'),
        104, 'ACTIVE', null, now(), null, null, '/logistic/bringingProducts', 101);


insert into r_forms(id, form_number, name, order_number, status, created_by, created_on,
                    modified_by, modified_on, href, parent_id)
VALUES (110, 110, row ('Производство', 'Ишлаб чиқариш', 'Ishlab chiqarish'), 110, 'ACTIVE', null, now(), null, null,
        null, null);

insert into r_forms(id, form_number, name, order_number, status, created_by, created_on,
                    modified_by, modified_on, href, parent_id)
VALUES (111, 111,
        row ("Аналогичная информация","Ўхшаш малумотлар","O'xshash malumotlar"),
        111, 'ACTIVE', null, now(), null, null, '/references/def_references', 110);

insert into r_forms(id, form_number, name, order_number, status, created_by, created_on,
                    modified_by, modified_on, href, parent_id)
VALUES (112, 112,
        row ('Готовый продукт', 'Тайёр маҳсулот', 'Tayyor mahsulot'),
        112, 'ACTIVE', null, now(), null, null, '/produce/ready-products', 110);

insert into r_forms(id, form_number, name, order_number, status, created_by, created_on,
                    modified_by, modified_on, href, parent_id)
VALUES (113, 113,
        row ('Затраты', 'Харажатлар', 'Xarajatlar'),
        113, 'ACTIVE', null, now(), null, null, '/produce/costs', 110);

insert into r_forms(id, form_number, name, order_number, status, created_by, created_on,
                    modified_by, modified_on, href, parent_id)
VALUES (114, 114,
        row ('I/CH qoldig''i', 'I/CH qoldig''i', 'I/CH qoldig''i'),
        114, 'ACTIVE', null, now(), null, null, '/produce/ready-products', 110);

insert into r_forms(id, form_number, name, order_number, status, created_by, created_on,
                    modified_by, modified_on, href, parent_id)
VALUES (120, 120, row ('Лидер Бетон', 'Леадер Бетон', 'Leader Beton'), 120, 'ACTIVE', null, now(), null, null,
        null, null);

insert into r_forms(id, form_number, name, order_number, status, created_by, created_on,
                    modified_by, modified_on, href, parent_id)
VALUES (121, 121,
        row ('Продажа', 'Сотиш', 'Sotish'),
        121, 'ACTIVE', null, now(), null, null, '/leader-beton/purchase', 120);


insert into r_forms(id, form_number, name, order_number, status, created_by, created_on,
                    modified_by, modified_on, href, parent_id)
VALUES (122, 122,
        row ('Список миксеры', 'Миксерлар рўйхати', 'Mikserlar ro''yxati'),
        122, 'ACTIVE', null, now(), null, null, '/leader-beton/mixers', 120);


insert into r_forms(id, form_number, name, order_number, status, created_by, created_on,
                    modified_by, modified_on, href, parent_id)
VALUES (123, 123,
        row ('Бетонный состав', 'Бетон таркиби', 'Beton tarkibi'),
        123, 'ACTIVE', null, now(), null, null, '/leader-beton/ingredients', 120);

insert into r_forms(id, form_number, name, order_number, status, created_by, created_on,
                    modified_by, modified_on, href, parent_id)
VALUES (124, 124,
        row ('Список продаж ', 'Сотувлар рўйҳати ', 'Sotuvlar ro''yhati'),
        124, 'ACTIVE', null, now(), null, null, '/leader-beton/purchase-list', 120);

insert into r_forms(id, form_number, name, order_number,  status, created_by, created_on,
                    modified_by, modified_on, href, parent_id)
VALUES (125, 125,
        row ('Остаток состав', 'Tаркиб қолдиғи', 'Tarkib qoldig''i'),
        125, 'ACTIVE', null, now(), null, null, '/leader-beton/remainder', 120);

insert into r_forms(id, form_number, name, order_number,  status, created_by, created_on,
                    modified_by, modified_on, href, parent_id)
VALUES (126, 126,
        row ('Mahsulot narxlari', 'Mahsulot narxlari', 'Mahsulot narxlari'),
        126, 'ACTIVE', null, now(), null, null, '/leader-beton/price', 120);


insert into r_forms(id, form_number, name, order_number, status, created_by, created_on,
                    modified_by, modified_on, href, parent_id)
VALUES (131, 131, row ('Xizmat kor''satish', 'Xizmat kor''satish', 'Xizmat kor''satish'), 131, 'ACTIVE', null, now(), null, null,
        null, 130);

insert into r_forms(id, form_number, name, order_number, status, created_by, created_on,
                    modified_by, modified_on, href, parent_id)
VALUES (132, 132, row ('Nasos xarajatlari', 'Nasos xarajatlari', 'Nasos xarajatlari'), 132, 'ACTIVE', null, now(), null, null,
        null, 130);

insert into r_forms(id, form_number, name, order_number, status, created_by, created_on,
                    modified_by, modified_on, href, parent_id)
VALUES (130, 130, row ('Насос', 'Насос', 'Nasos'), 130, 'ACTIVE', null, now(), null, null,
        '/nasos', null);

insert into r_forms(id, form_number, name, order_number, status, created_by, created_on,
                    modified_by, modified_on, href, parent_id)
VALUES (140, 140, row ('Зарплата', 'Иш ҳақи', 'Ish haqi'), 140, 'ACTIVE', null, now(), null, null,
        null, null);

insert into r_forms(id, form_number, name, order_number, status, created_by, created_on,
                    modified_by, modified_on, href, parent_id)
VALUES (141, 141, row ('Таблица', 'Табел', 'Tabel'), 140, 'ACTIVE', null, now(), null, null, '/tabel',
        141);

insert into r_forms(id, form_number, name, order_number,  status, created_by, created_on,
                    modified_by, modified_on, href, parent_id)
VALUES (142, 142,
        row ('Не отправляй', 'Ёъқлама', 'Yo''qlama'),
        142, 'ACTIVE', null, now(), null, null, '/forms/3', 140);

insert into r_forms(id, form_number, name, order_number,  status, created_by, created_on,
                    modified_by, modified_on, href, parent_id)
VALUES (143, 143,
        row ('Список рабочих', 'Ишчилар рўйхати', 'Ishchilar ro''yxati'),
        143, 'ACTIVE', null, now(), null, null, '/references/workers', 140);



insert into r_forms(id, form_number, name, order_number, status, created_by, created_on,
                    modified_by, modified_on, href, parent_id)
VALUES (150, 150, row ('Общая информация', 'Умумий маълумот', 'Umumiy ma''lumot'), 150, 'ACTIVE', null, now(), null, null,
        null, null);

insert into r_forms(id, form_number, name, order_number,  status, created_by, created_on,
                    modified_by, modified_on, href, parent_id)
VALUES (151, 151,
        row ('E/Q kirimi', 'E/Q kirimi', 'E/Q kirimi'),
        151, 'ACTIVE', null, now(), null, null, '/general/spare-part', 150);

insert into r_forms(id, form_number, name, order_number,  status, created_by, created_on,
                    modified_by, modified_on, href, parent_id)
VALUES (152, 152,
        row ('E/Q ombori', 'E/Q ombori', 'E/Q ombori'),
        152, 'ACTIVE', null, now(), null, null, '/general/warehouse', 150);

insert into r_forms(id, form_number, name, order_number,  status, created_by, created_on,
                    modified_by, modified_on, href, parent_id)
VALUES (153, 153,
        row ('E/Q sarfi', 'E/Q sarfi', 'E/Q sarfi'),
        153, 'ACTIVE', null, now(), null, null, '/general/spendings-spare-part', 150);

-------------------------------------------------------------------------------------------
-------------------------------------------------------------------------------------------
insert into r_forms(id, form_number, name, order_number, status, created_by, created_on,
                    modified_by, modified_on, href, parent_id)
VALUES (160, 160, row ('АДМИН ПАНЕЛЬ', 'АДМИН ПАНЕЛ', 'Admin panel'), 160, 'ACTIVE', null,
        now(), null, null, '/user-list', null);
-------------------------------------------------------------------------------------------
-------------------------------------------------------------------------------------------





