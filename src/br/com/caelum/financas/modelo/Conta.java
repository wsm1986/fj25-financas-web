package br.com.caelum.financas.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.caelum.financas.validator.NumeroEAgencia;

@NumeroEAgencia
@Entity
@Cacheable
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "agencia", "numero" }) })
public class Conta implements Serializable {

	private static final long serialVersionUID = 1L;

	@Version
	private Integer versao;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@NotNull
	private String titular;
	private String agencia;
	private String numero;
	@Column(length = 20, nullable = false)
	@Size(max = 3)
	@NotEmpty
	private String banco;

	@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	@OneToMany(mappedBy = "conta", fetch = FetchType.EAGER)
	private List<Movimentacao> movimentacoes = new ArrayList<Movimentacao>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitular() {
		return titular;
	}

	public void setTitular(String titular) {
		this.titular = titular;
	}

	public String getAgencia() {
		return agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getBanco() {
		return banco;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}

	public List<Movimentacao> getMovimentacoes() {
		return movimentacoes;
	}

	public void setMovimentacoes(List<Movimentacao> movimentacoes) {
		this.movimentacoes = movimentacoes;
	}

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

}
