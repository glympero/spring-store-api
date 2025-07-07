create table carts
(
    id          binary(16) default (uuid_to_bin(uuid())) not null
        primary key,
    dateCreated DATE       default (curdate())           not null
);

create table cart_items
(
    id         bigint auto_increment
        primary key,
    quantity   int        not null,
    product_id bigint       not null,
    cart_id    binary(16) not null,
    constraint cart_items_cart_product_unique
        unique (cart_id, product_id),
    constraint cart_items_carts_id_fk
        foreign key (cart_id) references carts (id)
            on delete cascade,
    constraint cart_items_products_id_fk
        foreign key (product_id) references products (id)
            on delete cascade
);