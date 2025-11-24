-- PACKAGE DE FUN��ES: PKG_CAPP_FUNCOES
------------------------------------------------------------
CREATE OR REPLACE PACKAGE PKG_CAPP_FUNCOES AS
  FUNCTION FN_VALIDAR_EMAIL (p_email IN VARCHAR2)
    RETURN NUMBER;
  FUNCTION FN_MEDIA_PROFESSOR (p_id_professor IN NUMBER)
    RETURN NUMBER;
  FUNCTION FN_PERC_AULAS_CONCLUIDAS_PROF (p_id_professor IN NUMBER)
    RETURN NUMBER;
END PKG_CAPP_FUNCOES;
/

CREATE OR REPLACE PACKAGE BODY PKG_CAPP_FUNCOES AS

  FUNCTION FN_VALIDAR_EMAIL (p_email IN VARCHAR2)
    RETURN NUMBER
  IS
  BEGIN
    IF p_email IS NULL OR
       NOT REGEXP_LIKE(p_email,
                       '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$')
    THEN
      RETURN 0;
    END IF;
    RETURN 1;
  EXCEPTION
    WHEN OTHERS THEN
      RETURN 0;
  END FN_VALIDAR_EMAIL;

  FUNCTION FN_MEDIA_PROFESSOR (p_id_professor IN NUMBER)
    RETURN NUMBER
  IS
    v_media NUMBER;
  BEGIN
    SELECT AVG(av.nota)
      INTO v_media
      FROM TB_CAPP_AVALIACAO av
      JOIN TB_CAPP_AULA a
        ON av.TB_CAPP_AULA_ID_aula = a.ID_aula
     WHERE a.TB_CAPP_PROFESSOR_ID_professor = p_id_professor;

    RETURN NVL(v_media, 0);
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      RETURN 0;
    WHEN TOO_MANY_ROWS THEN
      RETURN 0;
    WHEN OTHERS THEN
      RETURN 0;
  END FN_MEDIA_PROFESSOR;

  FUNCTION FN_PERC_AULAS_CONCLUIDAS_PROF (p_id_professor IN NUMBER)
    RETURN NUMBER
  IS
    v_total      NUMBER;
    v_concluidas NUMBER;
    v_perc       NUMBER;
  BEGIN
    SELECT COUNT(*)
      INTO v_total
      FROM TB_CAPP_AULA
     WHERE TB_CAPP_PROFESSOR_ID_professor = p_id_professor;

    SELECT COUNT(*)
      INTO v_concluidas
      FROM TB_CAPP_AULA
     WHERE TB_CAPP_PROFESSOR_ID_professor = p_id_professor
       AND status_aula = 'CONCLUIDA';

    v_perc := (v_concluidas / v_total) * 100;

    RETURN v_perc;
  EXCEPTION
    WHEN ZERO_DIVIDE THEN
      RETURN 0;
    WHEN NO_DATA_FOUND THEN
      RETURN 0;
    WHEN OTHERS THEN
      RETURN 0;
  END FN_PERC_AULAS_CONCLUIDAS_PROF;

END PKG_CAPP_FUNCOES;
/

------------------------------------------------------------
-- PACKAGE DE INSERTS: PKG_CAPP_INSERCOES
------------------------------------------------------------
CREATE OR REPLACE PACKAGE PKG_CAPP_INSERCOES AS
  PROCEDURE PRC_INS_AREA (
    p_id_area        IN TB_CAPP_AREA.ID_area%TYPE,
    p_nome_area      IN TB_CAPP_AREA.nome_area%TYPE,
    p_descricao_area IN TB_CAPP_AREA.descricao_area%TYPE
  );
  PROCEDURE PRC_INS_MODALIDADE (
    p_id_modalidade        IN TB_CAPP_MODALIDADE.ID_modalidade%TYPE,
    p_nome_modalidade      IN TB_CAPP_MODALIDADE.nome_modalidade%TYPE,
    p_descricao_modalidade IN TB_CAPP_MODALIDADE.descricao_modalidade%TYPE,
    p_id_area              IN TB_CAPP_AREA.ID_area%TYPE
  );
  PROCEDURE PRC_INS_PROFESSOR (
    p_id_professor    IN TB_CAPP_PROFESSOR.ID_professor%TYPE,
    p_nome            IN TB_CAPP_PROFESSOR.nome_professor%TYPE,
    p_email           IN TB_CAPP_PROFESSOR.email_professor%TYPE,
    p_senha           IN TB_CAPP_PROFESSOR.senha_professor%TYPE,
    p_telefone        IN TB_CAPP_PROFESSOR.telefone_professor%TYPE,
    p_bio             IN TB_CAPP_PROFESSOR.bio_professor%TYPE,
    p_experiencia     IN TB_CAPP_PROFESSOR.experiencia_professor%TYPE
  );
  PROCEDURE PRC_INS_ALUNO (
    p_id_aluno   IN TB_CAPP_ALUNO.ID_aluno%TYPE,
    p_nome       IN TB_CAPP_ALUNO.nome_aluno%TYPE,
    p_email      IN TB_CAPP_ALUNO.email_aluno%TYPE,
    p_senha      IN TB_CAPP_ALUNO.senha_aluno%TYPE,
    p_telefone   IN TB_CAPP_ALUNO.telefone_aluno%TYPE
  );
  PROCEDURE PRC_INS_AULA (
    p_id_aula        IN TB_CAPP_AULA.ID_aula%TYPE,
    p_status_aula    IN TB_CAPP_AULA.status_aula%TYPE,
    p_link_aula      IN TB_CAPP_AULA.Link_aula%TYPE,
    p_preco_aula     IN TB_CAPP_AULA.preco_aula%TYPE,
    p_qtd_hrs        IN TB_CAPP_AULA.qtd_hrs%TYPE,
    p_id_modalidade  IN TB_CAPP_MODALIDADE.ID_modalidade%TYPE,
    p_id_professor   IN TB_CAPP_PROFESSOR.ID_professor%TYPE,
    p_id_aluno       IN TB_CAPP_ALUNO.ID_aluno%TYPE
  );
  PROCEDURE PRC_INS_AVALIACAO (
    p_id_avaliacao IN TB_CAPP_AVALIACAO.ID_avaliacao%TYPE,
    p_nota         IN TB_CAPP_AVALIACAO.nota%TYPE,
    p_comentario   IN TB_CAPP_AVALIACAO.comentario%TYPE,
    p_data         IN TB_CAPP_AVALIACAO.data_avaliacao%TYPE,
    p_id_aula      IN TB_CAPP_AULA.ID_aula%TYPE
  );
END PKG_CAPP_INSERCOES;
/

CREATE OR REPLACE PACKAGE BODY PKG_CAPP_INSERCOES AS

  PROCEDURE PRC_INS_AREA (
    p_id_area        IN TB_CAPP_AREA.ID_area%TYPE,
    p_nome_area      IN TB_CAPP_AREA.nome_area%TYPE,
    p_descricao_area IN TB_CAPP_AREA.descricao_area%TYPE
  ) AS
  BEGIN
    INSERT INTO TB_CAPP_AREA (ID_area, nome_area, descricao_area)
    VALUES (p_id_area, p_nome_area, p_descricao_area);
    COMMIT;
  EXCEPTION
    WHEN DUP_VAL_ON_INDEX THEN
      RAISE_APPLICATION_ERROR(-20101, '�rea j� cadastrada.');
    WHEN OTHERS THEN
      RAISE_APPLICATION_ERROR(-20901, 'Erro ao inserir �rea: ' || SQLERRM);
  END PRC_INS_AREA;

  PROCEDURE PRC_INS_MODALIDADE (
    p_id_modalidade        IN TB_CAPP_MODALIDADE.ID_modalidade%TYPE,
    p_nome_modalidade      IN TB_CAPP_MODALIDADE.nome_modalidade%TYPE,
    p_descricao_modalidade IN TB_CAPP_MODALIDADE.descricao_modalidade%TYPE,
    p_id_area              IN TB_CAPP_AREA.ID_area%TYPE
  ) AS
  BEGIN
    INSERT INTO TB_CAPP_MODALIDADE (
      ID_modalidade, nome_modalidade, descricao_modalidade, TB_CAPP_AREA_ID_area
    ) VALUES (
      p_id_modalidade, p_nome_modalidade, p_descricao_modalidade, p_id_area
    );
    COMMIT;
  EXCEPTION
    WHEN DUP_VAL_ON_INDEX THEN
      RAISE_APPLICATION_ERROR(-20102, 'Modalidade j� cadastrada.');
    WHEN OTHERS THEN
      RAISE_APPLICATION_ERROR(-20902, 'Erro ao inserir modalidade: ' || SQLERRM);
  END PRC_INS_MODALIDADE;

  PROCEDURE PRC_INS_PROFESSOR (
    p_id_professor    IN TB_CAPP_PROFESSOR.ID_professor%TYPE,
    p_nome            IN TB_CAPP_PROFESSOR.nome_professor%TYPE,
    p_email           IN TB_CAPP_PROFESSOR.email_professor%TYPE,
    p_senha           IN TB_CAPP_PROFESSOR.senha_professor%TYPE,
    p_telefone        IN TB_CAPP_PROFESSOR.telefone_professor%TYPE,
    p_bio             IN TB_CAPP_PROFESSOR.bio_professor%TYPE,
    p_experiencia     IN TB_CAPP_PROFESSOR.experiencia_professor%TYPE
  ) AS
  BEGIN
    IF PKG_CAPP_FUNCOES.FN_VALIDAR_EMAIL(p_email) = 0 THEN
      RAISE_APPLICATION_ERROR(-20001, 'E-mail de professor inv�lido: ' || p_email);
    END IF;
    INSERT INTO TB_CAPP_PROFESSOR (
      ID_professor, nome_professor, email_professor, senha_professor,
      telefone_professor, data_cadastro, bio_professor, experiencia_professor
    ) VALUES (
      p_id_professor, p_nome, p_email, p_senha,
      p_telefone, SYSDATE, p_bio, p_experiencia
    );
    COMMIT;
  EXCEPTION
    WHEN DUP_VAL_ON_INDEX THEN
      RAISE_APPLICATION_ERROR(-20103, 'Professor j� cadastrado.');
    WHEN OTHERS THEN
      RAISE_APPLICATION_ERROR(-20903, 'Erro ao inserir professor: ' || SQLERRM);
  END PRC_INS_PROFESSOR;

  PROCEDURE PRC_INS_ALUNO (
    p_id_aluno   IN TB_CAPP_ALUNO.ID_aluno%TYPE,
    p_nome       IN TB_CAPP_ALUNO.nome_aluno%TYPE,
    p_email      IN TB_CAPP_ALUNO.email_aluno%TYPE,
    p_senha      IN TB_CAPP_ALUNO.senha_aluno%TYPE,
    p_telefone   IN TB_CAPP_ALUNO.telefone_aluno%TYPE
  ) AS
  BEGIN
    IF PKG_CAPP_FUNCOES.FN_VALIDAR_EMAIL(p_email) = 0 THEN
      RAISE_APPLICATION_ERROR(-20002, 'E-mail de aluno inv�lido: ' || p_email);
    END IF;
    INSERT INTO TB_CAPP_ALUNO (
      ID_aluno, nome_aluno, email_aluno, senha_aluno,
      telefone_aluno, data_cadastro
    ) VALUES (
      p_id_aluno, p_nome, p_email, p_senha,
      p_telefone, SYSDATE
    );
    COMMIT;
  EXCEPTION
    WHEN DUP_VAL_ON_INDEX THEN
      RAISE_APPLICATION_ERROR(-20104, 'Aluno j� cadastrado.');
    WHEN OTHERS THEN
      RAISE_APPLICATION_ERROR(-20904, 'Erro ao inserir aluno: ' || SQLERRM);
  END PRC_INS_ALUNO;

  PROCEDURE PRC_INS_AULA (
    p_id_aula        IN TB_CAPP_AULA.ID_aula%TYPE,
    p_status_aula    IN TB_CAPP_AULA.status_aula%TYPE,
    p_link_aula      IN TB_CAPP_AULA.Link_aula%TYPE,
    p_preco_aula     IN TB_CAPP_AULA.preco_aula%TYPE,
    p_qtd_hrs        IN TB_CAPP_AULA.qtd_hrs%TYPE,
    p_id_modalidade  IN TB_CAPP_MODALIDADE.ID_modalidade%TYPE,
    p_id_professor   IN TB_CAPP_PROFESSOR.ID_professor%TYPE,
    p_id_aluno       IN TB_CAPP_ALUNO.ID_aluno%TYPE
  ) AS
  BEGIN
    IF p_qtd_hrs IS NULL OR p_qtd_hrs <= 0 THEN
      RAISE_APPLICATION_ERROR(-20003, 'Quantidade de horas inv�lida.');
    END IF;
    INSERT INTO TB_CAPP_AULA (
      ID_aula, status_aula, Link_aula, preco_aula, qtd_hrs,
      TB_CAPP_MODALIDADE_ID_modalidade,
      TB_CAPP_PROFESSOR_ID_professor,
      TB_CAPP_ALUNO_ID_aluno
    ) VALUES (
      p_id_aula, p_status_aula, p_link_aula, p_preco_aula, p_qtd_hrs,
      p_id_modalidade, p_id_professor, p_id_aluno
    );
    COMMIT;
  EXCEPTION
    WHEN DUP_VAL_ON_INDEX THEN
      RAISE_APPLICATION_ERROR(-20105, 'Aula j� cadastrada.');
    WHEN INVALID_NUMBER THEN
      RAISE_APPLICATION_ERROR(-20106, 'N�mero inv�lido (pre�o ou horas).');
    WHEN OTHERS THEN
      RAISE_APPLICATION_ERROR(-20905, 'Erro ao inserir aula: ' || SQLERRM);
  END PRC_INS_AULA;

  PROCEDURE PRC_INS_AVALIACAO (
    p_id_avaliacao IN TB_CAPP_AVALIACAO.ID_avaliacao%TYPE,
    p_nota         IN TB_CAPP_AVALIACAO.nota%TYPE,
    p_comentario   IN TB_CAPP_AVALIACAO.comentario%TYPE,
    p_data         IN TB_CAPP_AVALIACAO.data_avaliacao%TYPE,
    p_id_aula      IN TB_CAPP_AULA.ID_aula%TYPE
  ) AS
  BEGIN
    IF p_nota < 1 OR p_nota > 5 THEN
      RAISE_APPLICATION_ERROR(-20004, 'Nota deve estar entre 1 e 5.');
    END IF;
    INSERT INTO TB_CAPP_AVALIACAO (
      ID_avaliacao, nota, comentario, data_avaliacao, TB_CAPP_AULA_ID_aula
    ) VALUES (
      p_id_avaliacao, p_nota, p_comentario, NVL(p_data, SYSDATE), p_id_aula
    );
    COMMIT;
  EXCEPTION
    WHEN DUP_VAL_ON_INDEX THEN
      RAISE_APPLICATION_ERROR(-20107, 'Avalia��o j� cadastrada.');
    WHEN OTHERS THEN
      RAISE_APPLICATION_ERROR(-20906, 'Erro ao inserir avalia��o: ' || SQLERRM);
  END PRC_INS_AVALIACAO;

END PKG_CAPP_INSERCOES;
COMMIT;
/

------------------------------------------------------------
-- BLOCO PARA INSERIR 10 REGISTROS EM CADA TABELA
-- USANDO AS PROCEDURES DO PACKAGE (COM COMMITS INTERNOS)
------------------------------------------------------------
BEGIN
  PKG_CAPP_INSERCOES.PRC_INS_AREA(1,  'Linguagens',              'Idiomas e comunica��o');
  PKG_CAPP_INSERCOES.PRC_INS_AREA(2,  'M�sica',                  'Instrumentos e teoria musical');
  PKG_CAPP_INSERCOES.PRC_INS_AREA(3,  'Tecnologia',              'Programa��o e computa��o');
  PKG_CAPP_INSERCOES.PRC_INS_AREA(4,  'Artes Visuais',           'Desenho e pintura');
  PKG_CAPP_INSERCOES.PRC_INS_AREA(5,  'Educa��o B�sica',         'Refor�o escolar');
  PKG_CAPP_INSERCOES.PRC_INS_AREA(6,  'Sa�de',                   'Sa�de e bem-estar');
  PKG_CAPP_INSERCOES.PRC_INS_AREA(7,  'Libras',                  'L�ngua Brasileira de Sinais');
  PKG_CAPP_INSERCOES.PRC_INS_AREA(8,  'Desenvolvimento Pessoal', 'Habilidades socioemocionais');
  PKG_CAPP_INSERCOES.PRC_INS_AREA(9,  'Matem�tica',              '�lgebra, fun��es, etc.');
  PKG_CAPP_INSERCOES.PRC_INS_AREA(10, 'Reda��o',                 'Produ��o de texto');

  PKG_CAPP_INSERCOES.PRC_INS_MODALIDADE(1,  'Ingl�s conversa��o iniciante', 'Pr�tica oral b�sica',             1);
  PKG_CAPP_INSERCOES.PRC_INS_MODALIDADE(2,  'Espanhol para viagens',         'Vocabul�rio de turismo',         1);
  PKG_CAPP_INSERCOES.PRC_INS_MODALIDADE(3,  'Viol�o iniciante',              'Acordes b�sicos e ritmo',        2);
  PKG_CAPP_INSERCOES.PRC_INS_MODALIDADE(4,  'Teoria musical b�sica',         'Leitura de partitura',           2);
  PKG_CAPP_INSERCOES.PRC_INS_MODALIDADE(5,  'Programa��o em Python',         'L�gica e sintaxe inicial',       3);
  PKG_CAPP_INSERCOES.PRC_INS_MODALIDADE(6,  'Desenho realista',              'Sombras e propor��es',           4);
  PKG_CAPP_INSERCOES.PRC_INS_MODALIDADE(7,  'Refor�o de Matem�tica 9� ano',  'Conte�do escolar fundamental',   5);
  PKG_CAPP_INSERCOES.PRC_INS_MODALIDADE(8,  'Introdu��o � Libras',           'No��es b�sicas de sinais',       7);
  PKG_CAPP_INSERCOES.PRC_INS_MODALIDADE(9,  'Reda��o para ENEM',             'Texto dissertativo-argumentativo',10);
  PKG_CAPP_INSERCOES.PRC_INS_MODALIDADE(10, 'L�gica de Programa��o',         'Pensamento computacional',       3);

  PKG_CAPP_INSERCOES.PRC_INS_PROFESSOR(1,  'Ana Souza',       'ana.souza@prof.com',       'senha123', '11999990001',
                                       'Professora de ingl�s focada em conversa��o.', '5 anos de experi�ncia');
  PKG_CAPP_INSERCOES.PRC_INS_PROFESSOR(2,  'Bruno Lima',      'bruno.lima@prof.com',      'senha123', '11999990002',
                                       'Professor de espanhol para viagens.',           '3 anos de interc�mbio');
  PKG_CAPP_INSERCOES.PRC_INS_PROFESSOR(3,  'Carla Martins',   'carla.martins@prof.com',   'senha123', '11999990003',
                                       'Professora de viol�o para iniciantes.',         '7 anos de aulas particulares');
  PKG_CAPP_INSERCOES.PRC_INS_PROFESSOR(4,  'Daniel Rocha',    'daniel.rocha@prof.com',    'senha123', '11999990004',
                                       'Professor de teoria musical.',                  'Formado em conservat�rio');
  PKG_CAPP_INSERCOES.PRC_INS_PROFESSOR(5,  'Eduardo Silva',   'eduardo.silva@prof.com',   'senha123', '11999990005',
                                       'Instrutor de Python e l�gica.',                 'Desenvolvedor backend');
  PKG_CAPP_INSERCOES.PRC_INS_PROFESSOR(6,  'Fernanda Alves',  'fernanda.alves@prof.com',  'senha123', '11999990006',
                                       'Artista pl�stica.',                              'Especialista em desenho realista');
  PKG_CAPP_INSERCOES.PRC_INS_PROFESSOR(7,  'Gabriel Santos',  'gabriel.santos@prof.com',  'senha123', '11999990007',
                                       'Professor de matem�tica.',                       'Licenciado em Matem�tica');
  PKG_CAPP_INSERCOES.PRC_INS_PROFESSOR(8,  'Helena Duarte',   'helena.duarte@prof.com',   'senha123', '11999990008',
                                       'Instrutora de Libras.',                          'Atua em escolas inclusivas');
  PKG_CAPP_INSERCOES.PRC_INS_PROFESSOR(9,  'Igor Menezes',    'igor.menezes@prof.com',    'senha123', '11999990009',
                                       'Professor de reda��o para vestibulares.',        'Corretor de ENEM');
  PKG_CAPP_INSERCOES.PRC_INS_PROFESSOR(10, 'Juliana Costa',   'juliana.costa@prof.com',   'senha123', '11999990010',
                                       'Instrutora de l�gica de programa��o.',           'Experi�ncia com iniciantes');

  PKG_CAPP_INSERCOES.PRC_INS_ALUNO(1,  'Lucas Pereira',    'lucas.pereira@aluno.com',   'senha123', '11988880001');
  PKG_CAPP_INSERCOES.PRC_INS_ALUNO(2,  'Marina Oliveira',  'marina.oliveira@aluno.com', 'senha123', '11988880002');
  PKG_CAPP_INSERCOES.PRC_INS_ALUNO(3,  'Rafaela Santos',   'rafaela.santos@aluno.com',  'senha123', '11988880003');
  PKG_CAPP_INSERCOES.PRC_INS_ALUNO(4,  'Jo�o Victor',      'joao.victor@aluno.com',     'senha123', '11988880004');
  PKG_CAPP_INSERCOES.PRC_INS_ALUNO(5,  'Beatriz Lima',     'beatriz.lima@aluno.com',    'senha123', '11988880005');
  PKG_CAPP_INSERCOES.PRC_INS_ALUNO(6,  'Thiago Costa',     'thiago.costa@aluno.com',    'senha123', '11988880006');
  PKG_CAPP_INSERCOES.PRC_INS_ALUNO(7,  'Camila Rocha',     'camila.rocha@aluno.com',    'senha123', '11988880007');
  PKG_CAPP_INSERCOES.PRC_INS_ALUNO(8,  'Felipe Souza',     'felipe.souza@aluno.com',    'senha123', '11988880008');
  PKG_CAPP_INSERCOES.PRC_INS_ALUNO(9,  'Isabela Nunes',    'isabela.nunes@aluno.com',   'senha123', '11988880009');
  PKG_CAPP_INSERCOES.PRC_INS_ALUNO(10, 'Pedro Almeida',    'pedro.almeida@aluno.com',   'senha123', '11988880010');

  PKG_CAPP_INSERCOES.PRC_INS_AULA(1,  'CONCLUIDA', 'https://sala1.com',  80, 1.5, 1, 1, 1);
  PKG_CAPP_INSERCOES.PRC_INS_AULA(2,  'CONCLUIDA', 'https://sala2.com',  90, 2.0, 2, 2, 2);
  PKG_CAPP_INSERCOES.PRC_INS_AULA(3,  'CONCLUIDA', 'https://sala3.com',  70, 1.0, 3, 3, 3);
  PKG_CAPP_INSERCOES.PRC_INS_AULA(4,  'CONCLUIDA', 'https://sala4.com',  85, 1.5, 4, 4, 4);
  PKG_CAPP_INSERCOES.PRC_INS_AULA(5,  'CONCLUIDA', 'https://sala5.com', 100, 2.0, 5, 5, 5);
  PKG_CAPP_INSERCOES.PRC_INS_AULA(6,  'CONCLUIDA', 'https://sala6.com',  95, 2.0, 6, 6, 6);
  PKG_CAPP_INSERCOES.PRC_INS_AULA(7,  'CONCLUIDA', 'https://sala7.com',  80, 1.0, 7, 7, 7);
  PKG_CAPP_INSERCOES.PRC_INS_AULA(8,  'CONCLUIDA', 'https://sala8.com',  90, 1.5, 8, 8, 8);
  PKG_CAPP_INSERCOES.PRC_INS_AULA(9,  'CONCLUIDA', 'https://sala9.com', 110, 2.0, 9, 9, 9);
  PKG_CAPP_INSERCOES.PRC_INS_AULA(10, 'CONCLUIDA', 'https://sala10.com',100, 1.5,10,10,10);

  PKG_CAPP_INSERCOES.PRC_INS_AVALIACAO(1,  5, 'Aula excelente, professor paciente.',             SYSDATE, 1);
  PKG_CAPP_INSERCOES.PRC_INS_AVALIACAO(2,  4, 'Muito boa, ajudou com vocabul�rio.',              SYSDATE, 2);
  PKG_CAPP_INSERCOES.PRC_INS_AVALIACAO(3,  5, 'Aprendi meus primeiros acordes.',                 SYSDATE, 3);
  PKG_CAPP_INSERCOES.PRC_INS_AVALIACAO(4,  4, 'Conte�do denso, mas bem explicado.',              SYSDATE, 4);
  PKG_CAPP_INSERCOES.PRC_INS_AVALIACAO(5,  5, 'Hist�ria ficou muito mais claro.',                  SYSDATE, 5);
  PKG_CAPP_INSERCOES.PRC_INS_AVALIACAO(6,  5, 'Professora incr�vel, desenho evoluiu muito.',     SYSDATE, 6);
  PKG_CAPP_INSERCOES.PRC_INS_AVALIACAO(7,  4, 'Matem�tica finalmente fez sentido.',              SYSDATE, 7);
  PKG_CAPP_INSERCOES.PRC_INS_AVALIACAO(8,  5, 'Aula inclusiva e acolhedora.',                    SYSDATE, 8);
  PKG_CAPP_INSERCOES.PRC_INS_AVALIACAO(9,  5, 'Reda��o corrigida com detalhes �timos.',          SYSDATE, 9);
  PKG_CAPP_INSERCOES.PRC_INS_AVALIACAO(10, 4, 'Introdu��o �tima � l�gica de programa��o.',       SYSDATE,10);
END;
COMMIT;
/