declare 
	-- parametros de entrada
	i_codsecao number(2,0);
	i_codtxtweb number(10,0);
	i_envelopecomprimido blob;
  	i_cpf varchar2(14);
  	
  	-- parametros de saida
  	o_status varchar2(32767) := NULL;
  	o_error varchar2(32767) := NULL;
  	
  	v_login varchar2(200) := NULL;
begin
	i_codsecao := ?;
	i_codtxtweb := ?;
	i_envelopecomprimido := ?;
	i_cpf := ?;

  	-- identifica o usuario perante o sistema de auditoria
  	select login into v_login from usuario where numcpf = i_cpf and IndAtivo = 'S';
  	dbms_session_set_context(v_login);
  	
	-- grava a assinatura e atualiza a situacao.
	update TextoWeb set ArqAssin = i_envelopecomprimido, CodSitua = nval_const('$$SituaTxtWebAssin') where	CodSecao = i_codsecao and CodTxtWeb = i_codtxtweb;
			
	o_status := 'OK';

	? := o_status;
	? := o_error;
end;