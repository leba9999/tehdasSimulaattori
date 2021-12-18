package simu.model;

/**
 * Enum luokka, jossa on kaikki mahdolliset tapahtuma tyypit, mitä tapahtuu simulaattorissa.
 * @author Leevi Koskinen, Sanna Kukkonen, Janne Lähteenmäki
 *
 */
public enum TapahtumanTyyppi {
	ARR1, // Saapumistapahtuma
	KPDEP, // Kasauspisteen poistuminen
	TP1DEP, // Testauspiste 1 poistuminen
	TP2DEP, // Testauspiste 2 poistuminen
	EPDEP, // Erikoiskaliprointi poistuminen
	PPDEP,// Pakkauspisteen poistuminen
	LPDEP,// Laaduntarkastuspiste poistuminen
	KJPDEP, // Korjauspisteen poistuminen

	// Palvelupisteiden saapumiset
	KPARR,
	TP1ARR,
	TP2ARR,
	EPARR,
	PPARR,
	LPARR,
	KJARR,
	// Palvelun aloitus tapahtumat
	KPPA,
	TP1PA,
	TP2PA,
	EPPA,
	PPPA,
	LPPA,
	KJPA,
	// Tuote poistuu simulaatiosta
	EX,
	// Animaatio välivaihe stoppeja varten
	AN
}
