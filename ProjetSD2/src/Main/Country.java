package Main;

public class Country {
	
	private String cca3;
	private String name;
	private int population;
	
	public Country(String cca3, String name, int population) {
		this.cca3 = cca3;
		this.name = name;
		this.population = population;
	}

	public String getCca3() {
		return cca3;
	}

	public String getName() {
		return name;
	}

	public int getPopulation() {
		return population;
	}

	@Override
	public String toString() {
		return "Country [cca3=" + cca3 + ", name=" + name + "]";
	}
	
}
