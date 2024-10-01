insert into USUARIOS  (id, username, password, role)
    values (100, 'admin@gmail.com', '$2a$12$Y3ypwyTD5Czhu7/sWaCU3uNa9VWk9vjIg6pTDelK1tDFdj/M8NIM6', 'ADMIN');
insert into USUARIOS  (id, username, password, role)
    values (101, 'bob@gmail.com', '$2a$12$UmfBvcgfNF3rCI9mj2kTVe5NpW0IMKQYoeOwBIvGk1nQpvW.XcX3C', 'USER');
insert into USUARIOS  (id, username, password, role)
    values (102, 'raissa@gmail.com', '$2a$12$Y3ypwyTD5Czhu7/sWaCU3uNa9VWk9vjIg6pTDelK1tDFdj/M8NIM6', 'USER');

insert into CLIENTES (id, nome, cpf, id_usuario) values (10, 'bob souza', '38352600060', 101);
insert into CLIENTES (id, nome, cpf, id_usuario) values (20, 'Raissa Mendes', '17908922015', 102);

insert into VAGAS (id, codigo, status ) values (100, 'A-01', 'OCUPADA');
insert into VAGAS (id, codigo, status ) values (200, 'A-02', 'OCUPADA');
insert into VAGAS (id, codigo, status ) values (300, 'A-03', 'OCUPADA');
insert into VAGAS (id, codigo, status ) values (400, 'A-04', 'LIVRE');
insert into VAGAS (id, codigo, status ) values (500, 'A-05', 'LIVRE');

insert into cliente_tem_vagas (numero_recibo, placa, marca, modelo, cor, data_entrada, id_cliente, id_vaga)
    values ('20241001-141519', 'ASD-8564', 'Honda', 'Civic Sport', 'Branco', '2024-10-01 14:37:19', 10, 100 );

insert into cliente_tem_vagas (numero_recibo, placa, marca, modelo, cor, data_entrada, id_cliente, id_vaga)
    values ('20241001-151519', 'FTA-0456', 'BMW', 'C6', 'Verde', '2024-10-01 14:37:19', 20, 200 );

insert into cliente_tem_vagas (numero_recibo, placa, marca, modelo, cor, data_entrada, id_cliente, id_vaga)
    values ('20241001-161019', 'SFQ-6547', 'Toyota', 'Bandeirante', 'Prata', '2024-10-01 14:37:19', 10, 300);