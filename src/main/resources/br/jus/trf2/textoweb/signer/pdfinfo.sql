DECLARE 
    i_codsecao  NUMBER(2, 0); 
    i_codtxtweb NUMBER(10, 0); 
    i_cpf       VARCHAR2(14); 
    o_pdf       BLOB := NULL; 
    o_secret    VARCHAR2(32767) := NULL; 
    o_status    VARCHAR2(32767) := NULL; 
    o_error     VARCHAR2(32767) := NULL; 
    v_login     VARCHAR2(200) := NULL; 
BEGIN 
    i_codsecao := ?; 

    i_codtxtweb := ?; 

    i_cpf := ?; 

    SELECT login 
    INTO   v_login 
    FROM   usuario 
    WHERE  numcpf = i_cpf 
           AND indativo = 'S'; 

    Dbms_session_set_context(v_login); 

    SELECT arq 
    INTO   o_pdf 
    FROM   textoweb tw 
    WHERE  tw.codsecao = i_codsecao 
           AND tw.codtxtweb = i_codtxtweb; 

    SELECT tw.coddoc 
           || To_char(tw.dthrincl, 'ddmmyyyyhh24miss') 
           || To_char(tw.dthrentr, 'ddmmyyyyhh24miss') 
           || tw.codlocfis 
    INTO   o_secret 
    FROM   textoweb tw 
    WHERE  tw.codsecao = i_codsecao 
           AND tw.codtxtweb = i_codtxtweb; 

    ? := o_pdf; 

    ? := o_secret; 

    ? := o_status; 

    ? := o_error; 
END; 