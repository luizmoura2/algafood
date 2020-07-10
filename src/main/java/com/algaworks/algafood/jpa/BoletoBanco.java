package com.algaworks.algafood.jpa;

import java.time.LocalDate;

public class BoletoBanco {

	private String codigoBanco;
	private String codigoMoeda;
	private LocalDate vencimento;
	private String fatorVencimento;
	private String codigoBarra;
	private String codigoBarraDig;
	private String nossoNumero;
	private String nomeBeneficiario;
	private String cpfcnpjBeneficiario;
	private String agenciaBeneficiario;
	private String dataDocumento;
	private String numeroDocumento;
	private String dataProcessamento;
	private String valorDocumento;
	
	public String getCodigoBanco() {
		return codigoBanco;
	}
	public void setCodigoBanco(String codigoBanco) {
		this.codigoBanco = codigoBanco;
	}
	public String getCodigoMoeda() {
		return codigoMoeda;
	}
	public void setCodigoMoeda(String codigoMoeda) {
		this.codigoMoeda = codigoMoeda;
	}
	public LocalDate getVencimento() {
		return vencimento;
	}
	public void setVencimento(LocalDate vencimento) {
		this.vencimento = vencimento;
	}
	public String getFatorVencimento() {
		return fatorVencimento;
	}
	public void setFatorVencimento(String fatorVencimento) {
		this.fatorVencimento = fatorVencimento;
	}
	public String getCodigoBarra() {
		return codigoBarra;
	}
	public void setCodigoBarra(String codigoBarra) {
		this.codigoBarra = codigoBarra;
	}
	public String getCodigoBarraDig() {
		return codigoBarraDig;
	}
	public void setCodigoBarraDig(String codigoBarraDig) {
		this.codigoBarraDig = codigoBarraDig;
	}
	public String getNossoNumero() {
		return nossoNumero;
	}
	public void setNossoNumero(String nossoNumero) {
		this.nossoNumero = nossoNumero;
	}
	public String getNomeBeneficiario() {
		return nomeBeneficiario;
	}
	public void setNomeBeneficiario(String nomeBeneficiario) {
		this.nomeBeneficiario = nomeBeneficiario;
	}
	public String getCpfcnpjBeneficiario() {
		return cpfcnpjBeneficiario;
	}
	public void setCpfcnpjBeneficiario(String cpfcnpjBeneficiario) {
		this.cpfcnpjBeneficiario = cpfcnpjBeneficiario;
	}
	public String getAgenciaBeneficiario() {
		return agenciaBeneficiario;
	}
	public void setAgenciaBeneficiario(String agenciaBeneficiario) {
		this.agenciaBeneficiario = agenciaBeneficiario;
	}
	public String getDataDocumento() {
		return dataDocumento;
	}
	public void setDataDocumento(String dataDocumento) {
		this.dataDocumento = dataDocumento;
	}
	public String getNumeroDocumento() {
		return numeroDocumento;
	}
	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}
	public String getDataProcessamento() {
		return dataProcessamento;
	}
	public void setDataProcessamento(String dataProcessamento) {
		this.dataProcessamento = dataProcessamento;
	}
	public String getValorDocumento() {
		return valorDocumento;
	}
	public void setValorDocumento(String valorDocumento) {
		this.valorDocumento = valorDocumento;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigoBanco == null) ? 0 : codigoBanco.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BoletoBanco other = (BoletoBanco) obj;
		if (codigoBanco == null) {
			if (other.codigoBanco != null)
				return false;
		} else if (!codigoBanco.equals(other.codigoBanco))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "BoletoBanco [codigoBanco=" + codigoBanco + ", codigoMoeda=" + codigoMoeda + ", vencimento=" + vencimento
				+ ", fatorVencimento=" + fatorVencimento + ", codigoBarra=" + codigoBarra + ", codigoBarraDig="
				+ codigoBarraDig + ", nossoNumero=" + nossoNumero + ", nomeBeneficiario=" + nomeBeneficiario
				+ ", cpfcnpjBeneficiario=" + cpfcnpjBeneficiario + ", agenciaBeneficiario=" + agenciaBeneficiario
				+ ", dataDocumento=" + dataDocumento + ", numeroDocumento=" + numeroDocumento + ", dataProcessamento="
				+ dataProcessamento + ", valorDocumento=" + valorDocumento + "]";
	}
	
	
}
