select tw.codsecao as idsecao,
       tw.codtxtweb as idtextoweb,
       tw.arq as arquivocompactado,
       ttw.descr as tipodetexto,
       formata_proc(p.numproccompl) as coddocumento,

  (select ti.descr
   from tipoincidente ti,
        processoincidente pi
   where pi.codsecao=tw.codsecao
     and tw.coddoc=pi.coddoc
     and pi.seqincid=tw.seqincid
     and ti.codsecao=pi.codsecao
     and ti.codtipincid=pi.codtipincid) as incidentedocumento, 
 tw.coddoc || to_char(tw.dthrincl, 'ddmmyyyyhh24miss') || to_char(tw.dthrentr, 'ddmmyyyyhh24miss') || tw.codlocfis as secret
from textoweb tw
inner join tipotextoweb ttw on ttw.codsecao = tw.codsecao
and ttw.codtiptxt = tw.codtiptxt
inner join t_processo p on p.codsecao = tw.codsecao
and p.coddoc = tw.coddoc
where tw.arq is not null
  and tw.arqassin is null
  and tw.codusuresp in
    (select m.codusu
     from magistrado m,
          pessoadocumento pd
     where pd.codtipdocpess = 1
       and pd.indativo = 'S'
       and m.codusu = tw.codusuresp
       and m.codsecao = pd.codsecao
       and m.codpess = pd.codpess
       and pd.numdocpess = ?)
  and tw.codsitua=4