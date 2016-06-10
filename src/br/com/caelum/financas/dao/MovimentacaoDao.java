package br.com.caelum.financas.dao;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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
		query.setHint("org.hibernate.cahceable", true);
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

	public List<Movimentacao> listaComCategorias() {
		String jpql = "select m from Movimentacao m left join fetch m.categorias";
		return this.manager.createQuery(jpql).getResultList();
	}

	public List<Movimentacao> lsitarMovimentacaoComCriteria() {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Movimentacao> criteria = builder.createQuery(Movimentacao.class);
		criteria.from(Movimentacao.class);
		return this.manager.createQuery(criteria).getResultList();
	}

	public BigDecimal somaMovimentacaoTitular(String titular) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<BigDecimal> criteria = builder.createQuery(BigDecimal.class);
		Root<Movimentacao> root = criteria.from(Movimentacao.class);
		criteria.select(builder.sum(root.<BigDecimal> get("valor")));
		criteria.where(builder.like(root.<Conta> get("conta").<String> get("titular"), "%" + titular + "%"));
		return this.manager.createQuery(criteria).getSingleResult();

	}

	public List<Movimentacao> pesquisa(Conta conta, TipoMovimentacao tipoMovimentacao, Integer mes) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Movimentacao> criteria = builder.createQuery(Movimentacao.class);
		Root<Movimentacao> root = criteria.from(Movimentacao.class);
		Predicate conjuction = builder.conjunction();
		if (conta.getId() != null) {
			conjuction = builder.and(conjuction, builder.equal(root.<Conta> get("conta"), conta));
		}
		if (mes != null && mes != 0) {
			Expression<Integer> expression = builder.function("month", Integer.class, root.<Calendar> get("data"));
			conjuction = builder.and(conjuction, builder.equal(expression, mes));
		}
		if (tipoMovimentacao != null) {
			conjuction = builder.and(conjuction,
					builder.equal(root.<TipoMovimentacao> get("tipoMovimentacao"), tipoMovimentacao));
		}
		criteria.where(conjuction);
		return this.manager.createQuery(criteria).getResultList();

	}
}
