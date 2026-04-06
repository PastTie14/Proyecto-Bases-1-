
--TABLESPACES
--TS_USERS      ? Seguridad y acceso
--TS_PETS       ? Core del negocio
--TS_FINANCIAL  ? Dinero
--TS_MATCHING   ? Crecimiento rápido
--TS_INDEXES    ? Rendimiento


-- TS_USER: Usuarios y autenticación
CREATE TABLESPACE TS_USER
    DATAFILE 'C:\app\Allan\oradata\Pr1\ts_user01.dbf'
    SIZE 10M
    REUSE
    AUTOEXTEND ON
    NEXT 512K
    MAXSIZE 200M;

-- TS_PET: Núcleo del sistema (más grande, más actividad)
CREATE TABLESPACE TS_PET
    DATAFILE 'C:\app\Allan\oradata\Pr1\ts_pet01.dbf'
    SIZE 20M
    REUSE
    AUTOEXTEND ON
    NEXT 1M
    MAXSIZE 500M;



-- TS_FINANCIAL: Donaciones y monedas
CREATE TABLESPACE TS_FINANCIAL
    DATAFILE 'C:\app\Allan\oradata\Pr1\ts_financial01.dbf'
    SIZE 10M
    REUSE
    AUTOEXTEND ON
    NEXT 512K
    MAXSIZE 200M;


-- TS_MATCHING: Sistema de matching (puede crecer rápido)
CREATE TABLESPACE TS_MATCHING
    DATAFILE 'C:\app\Allan\oradata\Pr1\ts_matching01.dbf'
    SIZE 15M
    REUSE
    AUTOEXTEND ON
    NEXT 1M
    MAXSIZE 300M;

-- TS_INDEXES: Índices separados (mejora I/O)
CREATE TABLESPACE TS_INDEX
    DATAFILE 'C:\app\Allan\oradata\Pr1\ts_index01.dbf'
    SIZE 15M
    REUSE
    AUTOEXTEND ON
    NEXT 1M
    MAXSIZE 300M;
    






