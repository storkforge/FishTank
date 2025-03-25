alter table appuser
    add constraint appuser_access_id_fk
        foreign key (accessid) references access;