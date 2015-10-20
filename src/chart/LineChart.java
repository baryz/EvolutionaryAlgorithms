package chart;

import java.awt.Color;


import org.jfree.chart.ChartFactory;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;


import evolutionaryAlgorithm.Statistic;

import org.jfree.chart.plot.XYPlot;

public class LineChart extends ApplicationFrame {
	
	private static final long serialVersionUID = 1L;
	private  final XYDataset dataset;
	private  final JFreeChart chart;
    public LineChart( String title, Statistic dataset) {

        super(title);

        this.dataset= createDataset( dataset );
        this.chart = createChart();
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);

    }
	
    private XYDataset createDataset(Statistic inDataset) {
        
    	double[] avgBasePopList = inDataset.getAvgBasePopulationList();
        XYSeries series1 = new XYSeries("AVG BASE FITNES");
        for(int i=0; i<avgBasePopList.length; i++){
        	series1.add(i,avgBasePopList[i]);
        }
    	
        double[] avgTempPopList = inDataset.getAvgTempPopulationList();
        XYSeries series2 = new XYSeries("AVG TEMP FITNES");
        for(int i=0; i<avgTempPopList.length; i++){
        	series2.add(i,avgTempPopList[i]);
        }
        
        int[] bestChromBasePopList = inDataset.getBestChromBaseList();
        XYSeries series3 = new XYSeries("BEST BASE FITNES");
        for(int i=0; i<bestChromBasePopList.length; i++){
        	series3.add(i,bestChromBasePopList[i]);
        }
        
        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1);
        dataset.addSeries(series2);
        dataset.addSeries(series3);
                
        return dataset;
        
    }
    
	public static void main(String[] args) {
        
	
	}
	
	private JFreeChart createChart() {
	   final JFreeChart chart = ChartFactory.createXYLineChart(
	            this.getTitle(),      // chart title
	            "No of population",                      // x axis label
	            "Fitnes",                      // y axis label
	            this.dataset,                  // data
	            PlotOrientation.VERTICAL,
	            true,                     // include legend
	            true,                     // tooltips
	            false                     // urls
	   );
	        
        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        
        double minYaxisValue = dataset.getYValue(0,0)- 1;
      
        
        ValueAxis yAxis = plot.getRangeAxis();
        
       yAxis.setRange(minYaxisValue,  yAxis.getUpperBound());
       
      
        
        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, false);
        renderer.setSeriesShapesVisible(1, false);
        plot.setRenderer(renderer);
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createStandardTickUnits());
       
                
        return chart;
	 }
}