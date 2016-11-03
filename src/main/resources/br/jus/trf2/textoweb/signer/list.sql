SELECT tw.codsecao                  AS IdSecao, 
       tw.codtxtweb                 AS IdTextoWeb, 
       tw.arq                       AS ArquivoCompactado, 
       ttw.descr                    AS TipoDeTexto, 
       Formata_proc(p.numproccompl) AS CodDocumento, 
       ti.descr                     AS IncidenteDocumento, 
       tw.coddoc 
       || To_char(tw.dthrincl, 'ddmmyyyyhh24miss') 
       || To_char(tw.dthrentr, 'ddmmyyyyhh24miss') 
       || tw.codlocfis              AS secret 
FROM   textoweb tw 
       INNER JOIN situacaotextoweb stw 
               ON stw.codsitua = Nval_const('$$SituaTxtWebRevis') 
                  AND stw.codsecao = tw.codsecao 
                  AND stw.codsitua = tw.codsitua 
       INNER JOIN tipotextoweb ttw 
               ON ttw.codsecao = tw.codsecao 
                  AND ttw.codtiptxt = tw.codtiptxt 
       INNER JOIN usuario u 
               ON u.codsecao = tw.codsecao 
                  AND u.codusu = tw.codusuresp 
       INNER JOIN t_processo p 
               ON p.codsecao = tw.codsecao 
                  AND p.coddoc = tw.coddoc 
       LEFT OUTER JOIN processoincidente pi 
                    ON pi.codsecao = tw.codsecao 
                       AND pi.coddoc = tw.coddoc 
                       AND pi.seqincid = tw.seqincid 
       LEFT OUTER JOIN tipoincidente ti 
                    ON ti.codsecao = pi.codsecao 
                       AND ti.codtipincid = pi.codtipincid 
WHERE  tw.arq IS NOT NULL 
       AND tw.arqassin IS NULL 
       AND tw.codusuresp IN (SELECT DISTINCT m.codusu 
                             FROM   magistrado m, 
                                    pessoadocumento pd 
                             WHERE  pd.codtipdocpess = 1 
                                    AND pd.indativo = 'S' 
                                    AND m.codusu = tw.codusuresp 
                                    AND m.codsecao = pd.codsecao 
                                    AND m.codpess = pd.codpess 
                                    AND pd.numdocpess = ?) 