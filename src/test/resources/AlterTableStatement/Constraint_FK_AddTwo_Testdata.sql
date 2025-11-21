    alter table [table1_name]
        add constraint [constraint1_name]
        foreign key ([column1_ID])
        references [table2_name] ([ID]),
        constraint [constraint2_name]
        foreign key ([column2_ID])
        references [table3_name] ([ID]);