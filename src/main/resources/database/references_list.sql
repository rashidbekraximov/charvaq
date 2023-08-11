insert into references_list(id, table_name, title)
values (1, 'r_product_type',
        row('Наименование товара',
            'Маҳсулот номи',
            'Mahsulot nomi')::t_nls);

insert into references_list(id, table_name,title)
values (2, 'r_communal_group',
        row('Тип утилиты',
            'Тип утилиты',
            'Kommunal turi')::t_nls);

insert into references_list(id, table_name, title)
values (3, 'r_status',
        row('Положение дел',
            'Ҳолати',
            'Holati')::t_nls);

insert into references_list(id, table_name, title)
values (4, 'r_currency_unit',
        row('Валютная единица',
            'Валютная единица',
            'Valyuta birligi')::t_nls);

insert into references_list(id, table_name, title)
values (5, 'r_mechanical_product',
        row('Запчасти',
            'Запчасти',
            'Extiyot qismlar')::t_nls);

insert into references_list(id, table_name, title)
values (6, 'r_seasons',
        row('Сезон',
            'Сезон',
            'Mavsum')::t_nls);

insert into references_list(id, table_name, title)
values (7, 'r_unit_of_measurements',
        row('Единица измерения',
            'Единица измерения',
            'Olchov birliki')::t_nls);

insert into references_list(id, table_name, title)
values (8, 'r_tax_type',
        row('Типы налогов',
            'Типы налогов',
            'Soliq turlari')::t_nls);

insert into references_list(id, table_name, title)
values (9, 'r_salary_type',
        row('Виды месячной заработной платы',
            'Виды месячной заработной платы',
            'Oyliq ish haqi turlari')::t_nls);
