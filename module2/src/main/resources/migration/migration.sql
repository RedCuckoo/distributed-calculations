CREATE TABLE "Product"
(
    "id"          serial       NOT NULL,
    "name"        VARCHAR(255) NOT NULL,
    "group_id"    serial       NOT NULL,
    "description" VARCHAR(255),
    "data"        DATE         NOT NULL,
    CONSTRAINT "Product_pk" PRIMARY KEY ("id")
) WITH (
      OIDS= FALSE
    );



CREATE TABLE "Group"
(
    "id"   serial       NOT NULL,
    "name" VARCHAR(255) NOT NULL,
    CONSTRAINT "Group_pk" PRIMARY KEY ("id")
) WITH (
      OIDS= FALSE
    );



CREATE TABLE "Group_ParameterGroup"
(
    "group_id"           integer NOT NULL,
    "parameter_group_id" integer NOT NULL
) WITH (
      OIDS= FALSE
    );



CREATE TABLE "ParameterGroup"
(
    "id"   serial       NOT NULL,
    "name" VARCHAR(255) NOT NULL,
    CONSTRAINT "ParameterGroup_pk" PRIMARY KEY ("id")
) WITH (
      OIDS= FALSE
    );



CREATE TABLE "Parameter"
(
    "id"   serial       NOT NULL,
    "name" VARCHAR(255) NOT NULL,
    "unit" VARCHAR(255) NOT NULL,
    CONSTRAINT "Parameter_pk" PRIMARY KEY ("id")
) WITH (
      OIDS= FALSE
    );



CREATE TABLE "Parameter_ParameterGroup"
(
    "parameter_id"      integer NOT NULL,
    "parametergroup_id" integer NOT NULL
) WITH (
      OIDS= FALSE
    );



CREATE TABLE "ParameterValue"
(
    "product_id"   integer NOT NULL,
    "parameter_id" integer NOT NULL,
    "value"        DECIMAL NOT NULL
) WITH (
      OIDS= FALSE
    );



ALTER TABLE "Product"
    ADD CONSTRAINT "Product_fk0" FOREIGN KEY ("group_id") REFERENCES "Group" ("id");


ALTER TABLE "Group_ParameterGroup"
    ADD CONSTRAINT "Group_ParameterGroup_fk0" FOREIGN KEY ("group_id") REFERENCES "Group" ("id");
ALTER TABLE "Group_ParameterGroup"
    ADD CONSTRAINT "Group_ParameterGroup_fk1" FOREIGN KEY ("parameter_group_id") REFERENCES "ParameterGroup" ("id");



ALTER TABLE "Parameter_ParameterGroup"
    ADD CONSTRAINT "Parameter_ParameterGroup_fk0" FOREIGN KEY ("parameter_id") REFERENCES "Parameter" ("id");
ALTER TABLE "Parameter_ParameterGroup"
    ADD CONSTRAINT "Parameter_ParameterGroup_fk1" FOREIGN KEY ("parametergroup_id") REFERENCES "ParameterGroup" ("id");

ALTER TABLE "ParameterValue"
    ADD CONSTRAINT "ParameterValue_fk0" FOREIGN KEY ("product_id") REFERENCES "Product" ("id");
ALTER TABLE "ParameterValue"
    ADD CONSTRAINT "ParameterValue_fk1" FOREIGN KEY ("parameter_id") REFERENCES "Parameter" ("id");

