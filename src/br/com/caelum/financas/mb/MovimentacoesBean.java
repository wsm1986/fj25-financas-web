package br.com.caelum.financas.mb;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.caelum.financas.dao.CategoriaDao;
import br.com.caelum.financas.dao.ContaDao;
import br.com.caelum.financas.dao.MovimentacaoDao;
import br.com.caelum.financas.modelo.Categoria;
import br.com.caelum.financas.modelo.Conta;
import br.com.caelum.financas.modelo.Movimentacao;
import br.com.caelum.financas.modelo.TipoMovimentacao;

@Named
@ViewScoped
public class MovimentacoesBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Movimentacao> movimentacoes;
	private List<Categoria> categorias;
	private Movimentacao movimentacao = new Movimentacao();
	private Integer contaId;
	private Integer categoriaId;

	@Inject
	private MovimentacaoDao movimentacaoDao;

	@Inject
	private ContaDao contaDao;

	@Inject
	private CategoriaDao categoriaDao;

	public void grava() {
		System.out.println("Fazendo a gravacao da movimentacao");
		Conta contaRelacionada = contaDao.busca(contaId);
		movimentacao.setConta(contaRelacionada);
		movimentacaoDao.adiciona(movimentacao);
		limpaFormularioDoJSF();
	}

	public void remove() {
		System.out.println("Removendo a movimentacao");
		movimentacaoDao.remove(movimentacao);
		limpaFormularioDoJSF();
	}

	public List<Movimentacao> getMovimentacoes() {
		if (movimentacoes == null) {
			// movimentacoes = movimentacaoDao.listaComCategorias(); Caso Lazy
			movimentacoes = movimentacaoDao.lista();
		}
		return movimentacoes;
	}

	public Movimentacao getMovimentacao() {
		if (movimentacao.getData() == null) {
			movimentacao.setData(Calendar.getInstance());
		}
		return movimentacao;
	}

	public void setMovimentacao(Movimentacao movimentacao) {
		this.movimentacao = movimentacao;
	}

	public Integer getContaId() {
		return contaId;
	}

	public void setContaId(Integer contaId) {
		this.contaId = contaId;
	}

	public Integer getCategoriaId() {
		return categoriaId;
	}

	public void setCategoriaId(Integer categoriaId) {
		this.categoriaId = categoriaId;
	}

	/**
	 * Esse metodo apenas limpa o formulario da forma com que o JSF espera.
	 * Invoque-o no momento manager que precisar do formulario vazio.
	 */
	private void limpaFormularioDoJSF() {
		this.movimentacao = new Movimentacao();
		movimentacoes = movimentacaoDao.lista();
	}

	public TipoMovimentacao[] getTiposDeMovimentacao() {
		return TipoMovimentacao.values();
	}

	public void setMovimentacoes(List<Movimentacao> movimentacoes) {
		this.movimentacoes = movimentacoes;
	}

	public void adicionaCategoria() {
		if (this.categoriaId != null && this.categoriaId > 0) {
			Categoria categoria = categoriaDao.procura(this.categoriaId);
			this.movimentacao.getCategorias().add(categoria);
		}
	}

	public List<Categoria> getCategorias() {
		if (categorias == null) {
			System.out.println("Listando Categorias");
			this.categorias = categoriaDao.lista();
		}
		return categorias;
	}

	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}
}
