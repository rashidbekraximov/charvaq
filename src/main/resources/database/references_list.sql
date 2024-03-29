insert into references_list(id, table_name, name)
values (1, 'r_product_type',
        row('Наименование товара',
            'Маҳсулот номи',
            'Mahsulot nomi')::t_nls);

insert into references_list(id, table_name, name)
values (4, 'r_drobilka_type',
        row('Drobilka turi',
            'Drobilka turi',
            'Drobilka turi')::t_nls);

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
