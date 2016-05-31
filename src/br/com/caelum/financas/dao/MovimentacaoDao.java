package br.com.caelum.financas.dao;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.com.caelum.financas.modelo.Conta;
import br.com.caelum.financas.modelo.Movimentacao;
import br.com.caelum.financas.modelo.TipoMovimentacao;
import br.com.caelum.financas.modelo.ValorPorMesEAno;

@Stateless
public class MovimentacaoDao {

	@Inject
	EntityManager manager;

	public void adiciona(Movimentacao movimentacao) {
		manager.joinTransaction();
		this.manager.persist(movimentacao);
	}

	public Movimentacao busca(Integer id) {
		return this.manager.find(Movimentacao.class, id);
	}

	public List<Movimentacao> lista() {
		return this.manager.createQuery("select m from Movimentacao m", Movimentacao.class).getResultList();
	}

	public void remove(Movimentacao movimentacao) {
		manager.joinTransaction();
		Movimentacao movimentacaoParaRemover = this.manager.find(Movimentacao.class, movimentacao.getId());
		this.manager.remove(movimentacaoParaRemover);
	}

	public List<Movimentacao> listaMovimentacoes(Conta conta) {
		String jpql = "select m from Movimentacao m " + "where m.conta = :conta order by m.valor desc";

		Query query = manager.createQuery(jpql);
		query.setParameter("conta", conta);
		return query.getResultList();
	}

	public List<Movimentacao> listaPorTipoEValor(BigDecimal valor, TipoMovimentacao tipoMovimentacao) {
		String jpql = "select m from Movimentacao m " + "where m.valor <= :valor and m.tipoMovimentacao =:tipo";

		Query query = manager.createQuery(jpql);
		query.setParameter("valor", valor);
		query.setParameter("tipo", tipoMovimentacao);
		return query.getResultList();
	}

	public BigDecimal calculaTotalMovimentado(Conta conta, TipoMovimentacao tipo) {
		String jpql = "select sum(m.valor) from Movimentacao m " + "where m.conta =:conta and m.tipoMovimentacao=:tipo";

		TypedQuery<BigDecimal> query = this.manager.createQuery(jpql, BigDecimal.class);
		query.setParameter("conta", conta);
		query.setParameter("tipo", tipo);
		return query.getSingleResult();
	}

	public List<Movimentacao> buscaTodasMovimentacoesDaConta(String titular) {
		String jpql = "select m from Movimentacao m " + "where m.conta.titular like :titular";

		TypedQuery<Movimentacao> query = this.manager.createQuery(jpql, Movimentacao.class);
		query.setParameter("titular", "%" + titular + "%");
		return query.getResultList();
	}

	public List<ValorPorMesEAno> listaMesesComMovimentacao(Conta conta, TipoMovimentacao tipoMovimentacao) {
		String jpql = "select new br.com.caelum.financas.modelo.ValorPorMesEAno("
				+ "month(m.data), year(m.data), sum(m.valor)) from Movimentacao m "
				+ "where m.conta =:conta and m.tipoMovimentacao=:tipo " + "group by year(m.data) || month(m.data) "
				+ "order by sum(m.valor) desc";

		TypedQuery<ValorPorMesEAno> query = this.manager.createQuery(jpql, ValorPorMesEAno.class);
		query.setParameter("conta", conta);
		query.setParameter("tipo", tipoMovimentacao);
		return query.getResultList();

	}
	public List<Movimentacao> listaComCategorias(){
		String jpql = "select m from Movimentacao m left join fetch m.categorias";
		return this.manager.createQuery(jpql).getResultList();
	}

}
