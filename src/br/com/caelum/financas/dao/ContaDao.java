package br.com.caelum.financas.dao;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import br.com.caelum.financas.modelo.Conta;

@Stateless
public class ContaDao {

	// @Resource
	// private UserTransaction ut;

	@PersistenceContext
	EntityManager manager;

	// Obriga a ter uma transação aberta
	// @TransactionAttribute(TransactionAttributeType.MANDATORY)
	public void adiciona(Conta conta) {
		this.manager.persist(conta);
	}

	public Conta busca(Integer id) {
		return this.manager.find(Conta.class, id);
	}

	public List<Conta> lista() {
		return this.manager.createQuery("select c from Conta c", Conta.class).getResultList();
	}

	public void remove(Conta conta) {
		Conta contaParaRemover = this.manager.find(Conta.class, conta.getId());
		this.manager.remove(contaParaRemover);
	}

	public void alterar(Conta conta) {
		this.manager.merge(conta);
	}

	public int TrocarNomeDoBancoEmLote(String nomeAntigo, String nomeNovo) {
		String jpql = "UPDATE Conta c Set c.banco =:nomeNovo where c.banco =:nomeAntigo";
		Query query = manager.createQuery(jpql);
		query.setParameter("nomeNovo", nomeNovo);
		query.setParameter("nomeAntigo", nomeAntigo);
		return query.executeUpdate();
	}
}
