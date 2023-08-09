insert into r_forms(id, form_number, name, order_number, status, created_by, created_on,
                    modified_by, modified_on, href, parent_id)
VALUES (100, 100, row ('Форма', 'Форма', 'Forma'), 100, 'ACTIVE', null, now(), null, null, null, null);

insert into r_forms(id, form_number, name, order_number,  status, created_by, created_on,
                    modified_by, modified_on, href, parent_id)
VALUES (1, 1,
        row ('Стоимость корма', 'Ем-хашак харажатлари', 'Yem-xashak xarajatlari'),
        1, 'ACTIVE', null, now(), null, null, '/forms/1', 100);

-----------------------------------------------------------------------------------------

insert into r_forms(id, form_number, name, order_number, status, created_by, created_on,
                    modified_by, modified_on, href, parent_id)
VALUES (101, 101, row ('Отчеты', 'Хисобот', 'Hisobot'), 101, 'ACTIVE', null, now(), null, null, null,
        null);

----------------------------------------------------------------------------------------
----------------------------------------------------------------------------------------

insert into r_forms(id, form_number, name, order_number, status, created_by, created_on,
                    modified_by, modified_on, href, parent_id)
VALUES (110, 110, row ('Информации', 'Маълумотлар', 'Malumotlar'), 110, 'ACTIVE', null, now(), null, null,
        null, null);

insert into r_forms(id, form_number, name, order_number, status, created_by, created_on,
                    modified_by, modified_on, href, parent_id)
VALUES (111, 111,
        row ('АНАЛОГИЧНАЯ ИНФОРМАЦИЯ', 'ЎХШАШ МАъЛУМОТЛАР', 'O''XSHASH MA''LUMOTLAR'),
        111, 'ACTIVE', null, now(), null, null, '/references/def_references', 110);

insert into r_forms(id, form_number, name, order_number, status, created_by, created_on,
                    modified_by, modified_on, href, parent_id)
VALUES (112, 112,
        row ('Налоги', 'Солиқлар', 'Soliqlar'),
        112, 'ACTIVE', null, now(), null, null, '/references/taxes', 110);

-------------------------------------------------------------------------------------------
-------------------------------------------------------------------------------------------
insert into r_forms(id, form_number, name, order_number, status, created_by, created_on,
                    modified_by, modified_on, href, parent_id)
VALUES (130, 130, row ('АДМИН ПАНЕЛЬ', 'АДМИН ПАНЕЛ', 'Admin panel'), 130, 'ACTIVE', null,
        now(), null, null, '/user-list', null);
-------------------------------------------------------------------------------------------
-------------------------------------------------------------------------------------------





