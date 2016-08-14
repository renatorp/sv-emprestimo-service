package br.pucminas.prod.entity;

public class Emprestimo {
	private Integer id;
	private Cliente solicitante;
	private ItemEmprestimo item;
	private Boolean finalziado;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Cliente getSolicitante() {
		return solicitante;
	}

	public void setSolicitante(Cliente solicitante) {
		this.solicitante = solicitante;
	}

	public ItemEmprestimo getItem() {
		return item;
	}

	public void setItem(ItemEmprestimo item) {
		this.item = item;
	}

	public Boolean getFinalziado() {
		return finalziado;
	}

	public void setFinalziado(Boolean finalziado) {
		this.finalziado = finalziado;
	}

}
