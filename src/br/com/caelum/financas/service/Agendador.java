package br.com.caelum.financas.service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.ScheduleExpression;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;

@Singleton
@Startup
public class Agendador {

	private static int totalCriado;

	@Resource
	private TimerService timerService;

	public void executa() {
		System.out.printf("%d instancias criadas %n", totalCriado);

		// simulando demora de 4s na execucao
		try {
			System.out.printf("Executando %s %n", this);
			Thread.sleep(4000);
		} catch (InterruptedException e) {
		}
	}

	@PostConstruct
	void posConstrucao() {
		System.out.println("Criando Agendador");
		totalCriado++;
	}

	@PreDestroy
	void preDestruicao() {
		System.out.println("Destruindo Agendador");
	}

	public void agenda(String minutos, String segundos) {
		ScheduleExpression expression = new ScheduleExpression();
		expression.hour("*");
		expression.minute(minutos);
		expression.second(segundos);
		TimerConfig timerConfig = new TimerConfig();
		timerConfig.setInfo(expression.toString());
		timerConfig.setPersistent(false);
		this.timerService.createCalendarTimer(expression, timerConfig);
		System.out.println("Agendamento: " + expression);
	}

	@Timeout
	public void verificaPeriodicaSeHaNovaConsulta(Timer timer) {
		System.out.println(timer.getInfo());

	}
	
	@Schedule(hour="*",minute="*/1", second="0", persistent=false)
	public void enviandoEmailCadaMinutoComInformacoesMov(){
		System.out.println("Enviando Email a cada minuto");
		
	}
}
