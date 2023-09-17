insert into references_list(id, table_name, name)
values (1, 'r_product_type',
        row('Наименование товара',
            'Маҳсулот номи',
            'Mahsulot nomi')::t_nls);

insert into references_list(id, table_name,name)
values (2, 'r_communal_group',
        row('Тип утилиты',
            'Тип утилиты',
            'Kommunal turi')::t_nls);

insert into references_list(id, table_name, name)
values (3, 'r_status',
        row('Положение дел',
            'Ҳолати',
            'Holati')::t_nls);

insert into references_list(id, table_name, name)
values (4, 'r_currency_unit',
        row('Валютная единица',
            'Валютная единица',
            'Valyuta birligi')::t_nls);

insert into references_list(id, table_name, name)
values (5, 'r_position',
        row('Позиция',
            'Лавозим',
            'Lavozim')::t_nls);

insert into references_list(id, table_name, name)
values (6, 'r_direction',
        row('Направление',
            'Ёъналиш',
            'Yo''nalish')::t_nls);

insert into references_list(id, table_name, name)
values (7, 'r_unit_of_measurements',
        row('Единица измерения',
            'Ўлчов бирлики',
            'O''lchov birliki')::t_nls);

insert into references_list(id, table_name, name)
values (8, 'r_tax_type',
        row('Типы налогов',
            'Типы налогов',
            'Soliq turlari')::t_nls);

insert into references_list(id, table_name, name)
values (9, 'r_technique_type',
        row('Тип техники',
            'Техника тури',
            'Texnika turi')::t_nls);

insert into references_list(id, table_name, name)
values (10, 'r_payment_type',
        row('Способ оплаты',
            'Тўлов тури',
            'To''lov turi')::t_nls);

insert into references_list(id, table_name, name)
values (11, 'r_cost_type',
        row('Вид расходов',
            'Харажат тури',
            'Xarajat turi')::t_nls);
