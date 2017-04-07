DECLARE 
    -- parametros de entrada 
    i_codsecao           NUMBER(2, 0); 
    i_codtxtweb          NUMBER(10, 0); 
    i_envelopecomprimido BLOB; 
    i_cpf                VARCHAR2(14); 
    -- parametros de saida 
    o_status             VARCHAR2(32767) := NULL; 
    o_error              VARCHAR2(32767) := NULL; 
    v_login              VARCHAR2(200) := NULL; 
    v_codusu             NUMBER(6, 0) := NULL; 
BEGIN 
    i_codsecao := ?; 

    i_codtxtweb := ?; 

    i_envelopecomprimido := ?; 

    i_cpf := ?; 

    -- identifica o usuario perante o sistema de auditoria 
    SELECT login, 
           codusu 
    INTO   v_login, v_codusu 
    FROM   usuario 
    WHERE  numcpf = i_cpf 
           AND indativo = 'S'; 

    Dbms_session_set_context(v_login); 

    -- grava a assinatura e atualiza a situacao. 
    UPDATE textoweb 
    SET    arqassin = i_envelopecomprimido, 
           codsitua = Nval_const('$$SituaTxtWebAssin'), 
           codusuresp = v_codusu 
    WHERE  codsecao = i_codsecao 
           AND codtxtweb = i_codtxtweb; 

    o_status := 'OK'; 

    ? := o_status; 

    ? := o_error; 
END; 