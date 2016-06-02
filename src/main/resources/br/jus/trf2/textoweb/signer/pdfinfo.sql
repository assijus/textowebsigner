declare 
	i_codsecao number(2,0);
	i_codtxtweb number(10,0);
  	i_cpf varchar2(14);
  	
	o_pdf blob := NULL;
  	o_status varchar2(32767) := NULL;
  	o_error varchar2(32767) := NULL;

  	v_login varchar2(200) := NULL;
begin
	i_codsecao := ?;
	i_codtxtweb := ?;
	i_cpf := ?;

  	select login into v_login from usuario where numcpf = i_cpf and IndAtivo = 'S';
  	dbms_session_set_context(v_login);
  	
  	select Arq into o_pdf from TextoWeb tw where tw.CodSecao = i_codsecao and tw.CodTxtWeb = i_codtxtweb;

  	? := o_pdf;
  		
	? := o_status;
	? := o_error;
end;