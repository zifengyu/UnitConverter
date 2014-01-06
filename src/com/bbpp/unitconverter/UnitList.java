package com.bbpp.unitconverter;

import java.io.Serializable;
import java.util.ArrayList;

import javax.measure.Measurable;
import javax.measure.Measure;
import javax.measure.quantity.*;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

public class UnitList<Q extends Quantity> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private ArrayList<Unit<Q>> unitList = new ArrayList<Unit<Q>>();
	private ArrayList<String> labelList = new ArrayList<String>();
	
	private Measurable<Q> value;
	private int baseUnitPosition;

	public String getLabel(int position) {
		if (position >= 0 && position < size())
			return labelList.get(position);		
		else
			return null;
	}
	
	public Unit<Q> getUnit(int position) {
		if (position >= 0 && position < size())
			return unitList.get(position);		
		else 
			return null;
	}	
	
	public double getValue(int position) {
		return value.doubleValue(unitList.get(position));		
	}
	
	public void setValue(double newValue, int unitPosition) {
		value = Measure.valueOf(newValue, unitList.get(unitPosition));
	}
	
	public void setBaseValue(double newValue) {
		setValue(newValue, baseUnitPosition);
	}

	public int size() {	
		return labelList.size();
	}

	private void add(String resourceId, Unit<Q> unit) {
		labelList.add(resourceId);
		unitList.add(unit);
	}
	
	public final static UnitList<Dimensionless> currencyList = new UnitList<Dimensionless>();
	static {
		currencyList.add("人民币\nCNY", Dimensionless.UNIT);
		currencyList.add("美元\nUSD", Dimensionless.UNIT);
		currencyList.add("英镑\nGBP", Dimensionless.UNIT);
		currencyList.add("欧元\nEUR", Dimensionless.UNIT);
		currencyList.add("港币\nHKD", Dimensionless.UNIT);		
		currencyList.add("澳门元\nMOP", Dimensionless.UNIT);
		currencyList.add("台币\nTWD", Dimensionless.UNIT);
		currencyList.add("日元\nJPY",Dimensionless.UNIT);
		currencyList.add("加拿大元\nCAD",Dimensionless.UNIT);
		currencyList.add("澳大利亚\nAUD",Dimensionless.UNIT);
		currencyList.add("新西兰元\nNZD",Dimensionless.UNIT);
		currencyList.add("瑞士法郎\nCHF", Dimensionless.UNIT);
		currencyList.add("新加坡元\nSGD", Dimensionless.UNIT);
		
		currencyList.baseUnitPosition = 1;
		currencyList.setBaseValue(0);
	}
	
	public static void setCurrencyRate(float[] rates) {
		for (int i = 0; i < rates.length; ++i) {
			if (i < currencyList.size()) {
				int index = i < currencyList.baseUnitPosition ? i : i + 1;
				currencyList.unitList.set(index, Dimensionless.UNIT.divide(rates[i]));
			}
		}
	}
	
	public final static UnitList<Length> lengthList = new UnitList<Length>();

	static {		
		lengthList.add("米\nm", SI.METER);
		lengthList.add("千米/公里\nkm", SI.KILOMETER);		
		lengthList.add("英里\nmi", NonSI.MILE);
		lengthList.add("码\nyd", NonSI.YARD);
		lengthList.add("英尺\nft", NonSI.FOOT);
		lengthList.add("英寸\nin", NonSI.INCH);
		lengthList.add("海里\nnmi", NonSI.NAUTICAL_MILE);		
		lengthList.add("市尺", SI.METER.divide(3));
		lengthList.add("市寸", SI.METER.divide(30));
		lengthList.add("化朗/浪\nfurlong", NonSI.MILE.divide(8));
		lengthList.add("英寻\nftm", NonSI.YARD.times(2));		
		lengthList.add("光年\nly", NonSI.LIGHT_YEAR);
		lengthList.add("天文单位\nAU", NonSI.ASTRONOMICAL_UNIT);
		lengthList.add("分米\ndm", SI.DECI(SI.METER));
		lengthList.add("厘米\ncm", SI.CENTIMETER);
		lengthList.add("毫米\nmm", SI.MILLIMETER);
		lengthList.add("微米\nμm", SI.MICRO(SI.METER));
		lengthList.add("纳米\nnm", SI.NANO(SI.METER));
		lengthList.add("埃\nÅ", NonSI.ANGSTROM);	
				
		lengthList.baseUnitPosition = 0;
		lengthList.setBaseValue(0);
	}

	public final static UnitList<Area> areaList = new UnitList<Area>();

	static {		
		areaList.add("平方米\nm²", SI.SQUARE_METRE);
		areaList.add("平方公里\nkm²", SI.SQUARE_METRE.times(1000000));		
		areaList.add("公顷\nha", NonSI.HECTARE);
		areaList.add("公亩\na", NonSI.ARE);
		areaList.add("平方英里\nmi²", SI.SQUARE_METRE.times(2589988110336L).divide(1000000));
		areaList.add("英亩\nac", SI.SQUARE_METRE.times(2589988110336L).divide(640000000));
		areaList.add("平方竿\nrd²", SI.SQUARE_METRE.times(2589988110336L).divide(102400000000L));
		areaList.add("平方码\nyd²", SI.SQUARE_METRE.times(2589988110336L).divide(3097600000000L));
		areaList.add("平方英尺\nft²", SI.SQUARE_METRE.times(2589988110336L).divide(27878400000000L));
		areaList.add("平方英寸\nin²", SI.SQUARE_METRE.times(2589988110336L).divide(4014489600000000L));
		areaList.add("市顷", SI.SQUARE_METRE.times(1000000).divide(15));
		areaList.add("市亩", SI.SQUARE_METRE.times(10000).divide(15));
		
		areaList.baseUnitPosition = 0;
		areaList.setBaseValue(0);
	}
	
	public final static UnitList<Volume> volumeList = new UnitList<Volume>();

	static {		
		volumeList.add("立方米\nm³", SI.CUBIC_METRE);
		volumeList.add("升\nL", NonSI.LITER);
		volumeList.add("毫升\nmL", NonSI.LITER.divide(1000));
		volumeList.add("立方英寸\nin³", NonSI.CUBIC_INCH);
		volumeList.add("石油桶\nbbl", NonSI.GALLON_LIQUID_US.times(42));
		volumeList.add("蒲式耳\nbsh", NonSI.GALLON_UK.times(8));
		volumeList.add("美加仑\nUS gal", NonSI.GALLON_LIQUID_US);
		volumeList.add("英加仑\nUK gal", NonSI.GALLON_UK);
		volumeList.add("夸脱\nqt", NonSI.OUNCE_LIQUID_US.times(32));
		volumeList.add("品脱\npt", NonSI.OUNCE_LIQUID_US.times(16));				
		volumeList.add("美制盎司\nfl oz", NonSI.OUNCE_LIQUID_US);		
		volumeList.add("英制盎司\nfl oz", NonSI.OUNCE_LIQUID_UK);
		volumeList.add("杯\nc", NonSI.OUNCE_LIQUID_US.times(8));
		volumeList.add("汤匙\ntbs", NonSI.OUNCE_LIQUID_US.divide(2));
		volumeList.add("茶匙\ntsp", NonSI.OUNCE_LIQUID_US.divide(6));
		
		volumeList.baseUnitPosition = 0;
		volumeList.setBaseValue(0);
	}
	
	public final static UnitList<Mass> massList = new UnitList<Mass>();

	static {		
		massList.add("千克\nkg", SI.KILOGRAM);
		massList.add("磅\nlb", NonSI.POUND);
		massList.add("盎司\noz", NonSI.OUNCE);	
		massList.add("克拉\nct", SI.GRAM.divide(5));
		massList.add("市斤", SI.GRAM.times(500));
		massList.add("市两", SI.GRAM.times(50));
		massList.add("市钱", SI.GRAM.times(5));
		massList.add("吨\nton", NonSI.METRIC_TON);
		massList.add("克\ng", SI.GRAM);		
		massList.add("原子量\nu", NonSI.ATOMIC_MASS);
		massList.baseUnitPosition = 0;
		massList.setBaseValue(0);
	}
	
	public final static UnitList<Temperature> temperatureList = new UnitList<Temperature>();

	static {		
		temperatureList.add("摄氏度\n℃", SI.CELSIUS);
		temperatureList.add("华氏度\n°F", NonSI.FAHRENHEIT);		
		temperatureList.add("开尔文\nK", SI.KELVIN);
		
		temperatureList.baseUnitPosition = 0;
		temperatureList.setBaseValue(0);
	}
	
	public final static UnitList<Duration> durationList = new UnitList<Duration>();

	static {		
		durationList.add("年\nyr", NonSI.YEAR_CALENDAR);
		durationList.add("周\nweek", NonSI.WEEK);
		durationList.add("天\nd", NonSI.DAY);
		durationList.add("小时\nh", NonSI.HOUR);
		durationList.add("分\nmin", NonSI.MINUTE);
		durationList.add("秒\ns", SI.SECOND);
		durationList.add("毫秒\nms", SI.MILLI(SI.SECOND));
		durationList.add("微秒\nμs", SI.MICRO(SI.SECOND));
		durationList.add("纳秒\nns", SI.NANO(SI.SECOND));
		durationList.add("皮秒\nps", SI.PICO(SI.SECOND));
		durationList.add("飞秒\nfs", SI.FEMTO(SI.SECOND));
		
		durationList.baseUnitPosition = 5;
		durationList.setBaseValue(0);
	}
	
	public final static UnitList<Velocity> velocityList = new UnitList<Velocity>();

	static {		
		velocityList.add("米/秒\nm/s", SI.METRES_PER_SECOND);		
		velocityList.add("公里/小时\nkph", NonSI.KILOMETERS_PER_HOUR);
		velocityList.add("英里/小时\nmph", NonSI.MILES_PER_HOUR);
		velocityList.add("英尺/秒\nft/s", NonSI.MILES_PER_HOUR.times(15).divide(22));
		velocityList.add("英寸/秒\nin/s", NonSI.MILES_PER_HOUR.times(5).divide(88));
		velocityList.add("节\nkn", NonSI.KNOT);
		velocityList.add("马赫\nmach", NonSI.MACH);
		velocityList.add("光速\nc", NonSI.C);
		
		velocityList.baseUnitPosition = 0;
		velocityList.setBaseValue(0);
	}
	
	public final static UnitList<Angle> angleList = new UnitList<Angle>();
	static {		
		angleList.add("度\n°", NonSI.DEGREE_ANGLE);		
		angleList.add("分\n′", NonSI.MINUTE_ANGLE);
		angleList.add("秒\n\"", NonSI.SECOND_ANGLE);
		angleList.add("弧度\nrad", SI.RADIAN);
		
		angleList.baseUnitPosition = 3;
		angleList.setBaseValue(0);
	}
	
	public final static UnitList<Energy> energyList = new UnitList<Energy>();	
	static {		
		energyList.add("千卡/大卡\nkcal", SI.JOULE.times(4184));
		energyList.add("卡\ncal", SI.JOULE.times(523).divide(125));
		energyList.add("焦耳\nJ", SI.JOULE);		
		energyList.add("尔格\nerg′", NonSI.ERG);
		energyList.add("电子伏特\neV", NonSI.ELECTRON_VOLT);
			
		energyList.baseUnitPosition = 2;
		energyList.setBaseValue(0);
	}
	
	public final static UnitList<Power> powerList = new UnitList<Power>();
	static {		
		powerList.add("千瓦\nkW", SI.KILO(SI.WATT));
		powerList.add("瓦\nW", SI.WATT);
		powerList.add("马力\nhp", NonSI.HORSEPOWER);
				
		powerList.baseUnitPosition = 1;
		powerList.setBaseValue(0);
	}
	
	public final static UnitList<Pressure> pressureList = new UnitList<Pressure>();
	static {
		pressureList.add("兆帕\nPa", SI.MEGA((SI.PASCAL)));
		pressureList.add("千帕\nPa", SI.KILO(SI.PASCAL));
		pressureList.add("帕\nPa", SI.PASCAL);
		pressureList.add("标准大气压\natm", NonSI.ATMOSPHERE);		
		pressureList.add("毫米汞柱\nmmHg", NonSI.MILLIMETER_OF_MERCURY);
		pressureList.add("英寸汞柱\ninHg", NonSI.INCH_OF_MERCURY);
		pressureList.add("巴\nbar", NonSI.BAR);
		
		pressureList.baseUnitPosition = 2;
		pressureList.setBaseValue(0);
	}
	
	public final static UnitList<Force> forceList = new UnitList<Force>();
	static {
		forceList.add("牛顿\nN", SI.NEWTON);
		forceList.add("千克力\nkgf", NonSI.KILOGRAM_FORCE);
		forceList.add("磅力\nlbf", NonSI.POUND_FORCE);
		forceList.add("达因\ndyn", NonSI.DYNE);
		
		forceList.baseUnitPosition = 0;
		forceList.setBaseValue(0);
	}

}
