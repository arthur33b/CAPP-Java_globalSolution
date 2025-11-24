--TABLES
-- DROP DAS TABELAS
------------------------------------------------------------
DROP TABLE TB_CAPP_AVALIACAO CASCADE CONSTRAINTS;
DROP TABLE TB_CAPP_AULA      CASCADE CONSTRAINTS;
DROP TABLE TB_CAPP_ALUNO     CASCADE CONSTRAINTS;
DROP TABLE TB_CAPP_PROFESSOR CASCADE CONSTRAINTS;
DROP TABLE TB_CAPP_MODALIDADE CASCADE CONSTRAINTS;
DROP TABLE TB_CAPP_AREA      CASCADE CONSTRAINTS;

------------------------------------------------------------
-- CRIAÇÃO DAS TABELAS
------------------------------------------------------------
CREATE TABLE TB_CAPP_AREA (
    ID_area        INTEGER        PRIMARY KEY,
    nome_area      VARCHAR2(100)  NOT NULL,
    descricao_area VARCHAR2(100)
);

CREATE TABLE TB_CAPP_MODALIDADE (
    ID_modalidade        INTEGER        PRIMARY KEY,
    nome_modalidade      VARCHAR2(100)  NOT NULL,
    descricao_modalidade VARCHAR2(200),
    TB_CAPP_AREA_ID_area INTEGER        NOT NULL,
    CONSTRAINT TB_CAPP_MODALIDADE_TB_CAPP_AREA_FK
        FOREIGN KEY (TB_CAPP_AREA_ID_area)
        REFERENCES TB_CAPP_AREA (ID_area)
);

CREATE TABLE TB_CAPP_PROFESSOR (
    ID_professor          INTEGER        PRIMARY KEY,
    nome_professor        VARCHAR2(100)  NOT NULL,
    email_professor       VARCHAR2(50)   NOT NULL,
    senha_professor       VARCHAR2(50)   NOT NULL,
    telefone_professor    VARCHAR2(20),
    data_cadastro         DATE           DEFAULT SYSDATE,
    bio_professor         VARCHAR2(200),
    experiencia_professor VARCHAR2(100)
);

CREATE TABLE TB_CAPP_ALUNO (
    ID_aluno       INTEGER        PRIMARY KEY,
    nome_aluno     VARCHAR2(100)  NOT NULL,
    email_aluno    VARCHAR2(50)   NOT NULL,
    senha_aluno    VARCHAR2(50)   NOT NULL,
    telefone_aluno VARCHAR2(20),
    data_cadastro  DATE           DEFAULT SYSDATE
);

CREATE TABLE TB_CAPP_AULA (
    ID_aula                          INTEGER        PRIMARY KEY,
    status_aula                      VARCHAR2(20)   NOT NULL,
    Link_aula                        VARCHAR2(200),
    preco_aula                       NUMBER(10,2)   NOT NULL,
    qtd_hrs                          NUMBER(4,2),
    TB_CAPP_MODALIDADE_ID_modalidade INTEGER       NOT NULL,
    TB_CAPP_PROFESSOR_ID_professor   INTEGER       NOT NULL,
    TB_CAPP_ALUNO_ID_aluno           INTEGER       NOT NULL,
    CONSTRAINT TB_CAPP_AULA_TB_CAPP_MODALIDADE_FK
        FOREIGN KEY (TB_CAPP_MODALIDADE_ID_modalidade)
        REFERENCES TB_CAPP_MODALIDADE (ID_modalidade),
    CONSTRAINT TB_CAPP_AULA_TB_CAPP_PROFESSOR_FK
        FOREIGN KEY (TB_CAPP_PROFESSOR_ID_professor)
        REFERENCES TB_CAPP_PROFESSOR (ID_professor),
    CONSTRAINT TB_CAPP_AULA_TB_CAPP_ALUNO_FK
        FOREIGN KEY (TB_CAPP_ALUNO_ID_aluno)
        REFERENCES TB_CAPP_ALUNO (ID_aluno)
);

CREATE TABLE TB_CAPP_AVALIACAO (
    ID_avaliacao      INTEGER        PRIMARY KEY,
    nota              INTEGER        NOT NULL,
    comentario        VARCHAR2(100),
    data_avaliacao    DATE           DEFAULT SYSDATE NOT NULL,
    TB_CAPP_AULA_ID_aula INTEGER     NOT NULL,
    CONSTRAINT TB_CAPP_AVALIACAO_TB_CAPP_AULA_FK
        FOREIGN KEY (TB_CAPP_AULA_ID_aula)
        REFERENCES TB_CAPP_AULA (ID_aula)
);
