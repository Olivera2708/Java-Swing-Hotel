package chart;

import org.knowm.xchart.internal.chartpart.Chart;

public interface MainChart<C extends Chart<?, ?>> {
	C getChart();
}