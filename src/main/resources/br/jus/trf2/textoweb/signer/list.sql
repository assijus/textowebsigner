select	tw.CodSecao as IdSecao, tw.CodTxtWeb as IdTextoWeb, tw.Arq as ArquivoCompactado, ttw.Descr as TipoDeTexto, formata_proc(p.numproccompl) as CodDocumento, ti.Descr as IncidenteDocumento
from	TextoWeb tw
inner 	join SituacaoTextoWeb stw on stw.CodSitua = nval_const('$$SituaTxtWebRevis') and stw.CodSecao = tw.CodSecao and stw.CodSitua = tw.Codsitua
inner	join TipoTextoWeb ttw on ttw.CodSecao = tw.CodSecao and ttw.CodTipTxt = tw.CodTipTxt
inner 	join Usuario u on u.CodSecao = tw.CodSecao and u.CodUsu = tw.CodUsuResp
inner	join t_Processo p on p.CodSecao = tw.CodSecao and p.CodDoc = tw.CodDoc
left	outer join ProcessoIncidente pi on pi.CodSecao = tw.CodSecao and pi.CodDoc = tw.CodDoc and pi.SeqIncid = tw.SeqIncid
left	outer join TipoIncidente ti on ti.CodSecao = pi.CodSecao and ti.CodTipIncid = pi.CodTipIncid
where	tw.Arq is not null
and	tw.ArqAssin is null
and	tw.CodUsuResp in
	(
		select	distinct m.codusu
		from 	magistrado m, pessoadocumento pd
		where	pd.CodtipDocPess = 1
		and	pd.IndAtivo = 'S'
		and	m.codusu = tw.CodUsuResp
		and	m.codsecao = pd.codsecao
		and	m.codpess = pd.codpess
		and	pd.NumDocPess = ?
	)
