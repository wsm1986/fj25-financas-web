package br.com.caelum.financas.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.com.caelum.financas.modelo.Categoria;

@Stateless
public class CategoriaDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	public Categoria procura(Integer id){
		return entityManager.find(Categoria.class, id);
	}
	public List<Categoria> lista(){
		String jpql = "select c from Categoria c";
		return entityManager.createQuery(jpql).getResultList();
	}
}
