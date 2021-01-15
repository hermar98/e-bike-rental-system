package main.java.com.team15.ebrs.util;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Used to create charts for the statistics GUI
 *
 * @see JFreeChart
 * @see ChartFactory
 * @see PlotOrientation
 * @see DefaultCategoryDataset
 * @see DefaultPieDataset
 * @see PieSectionLabelGenerator
 * @see StandardPieSectionLabelGenerator
 * @see PiePlot
 */
public class Charts {
    private static Color bg = Color.WHITE;//decode("#1766be");

    /**
     * This method creates a bar chart with only one dataset.
     *
     * It takes a title, a label for the value axis, a label for the category axis and a default category dataset as parameters.
     * @param title the title of the chart.
     * @param valueAxis the label for the value axis.
     * @param catAxis the label for the category axis.
     * @param dataset the dataset for the bar chart.
     * @return a JFreeChart Bar chart.
     * @see JFreeChart
     * @see DefaultCategoryDataset
     */
    public static JFreeChart createBarChart(String title, String valueAxis, String catAxis, DefaultCategoryDataset dataset) {
        ArrayList<DefaultCategoryDataset> datasets = new ArrayList<>();
        datasets.add(dataset);
        return createBarChart(title, valueAxis, catAxis, datasets);
    }

    /**
     * This method creates a bar chart with only one dataset.
     *
     * It takes a title, a label for the value axis, a label for the category axis and an ArrayList of default category datasets as parameters.
     * @param title the title of the chart.
     * @param valueAxis the label for the value axis.
     * @param catAxis the label for the category axis.
     * @param datasets an ArrayList of datasets for the bar charts.
     * @return a JFreeChart Bar Chart.
     * @see JFreeChart
     * @see DefaultCategoryDataset
     * @see ChartFactory
     */
    public static JFreeChart createBarChart(String title, String valueAxis, String catAxis, ArrayList<DefaultCategoryDataset> datasets) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < datasets.size(); i++) {
            for (int j = 0; j < datasets.get(i).getRowCount(); j++) {
                dataset.setValue(datasets.get(i).getValue(datasets.get(i).getRowKey(j), datasets.get(i).getColumnKey(0)), datasets.get(i).getRowKey(j), datasets.get(i).getColumnKey(0));
            }
        }


        JFreeChart barChart = ChartFactory.createBarChart(title, catAxis, valueAxis, dataset,
                PlotOrientation.VERTICAL,
                true, true, false);
        barChart.setBackgroundPaint(bg);
        return barChart;
    }

    /**
     * This method creates a dataset for a bar chart. It is used to create the datasets for the different bar chart statistics.
     *
     * It takes an ArrayList of numbers (values), an ArrayList of names (categories) and an ArrayList of labels (label different types of bars on the chart) as parameters.
     *
     * The numbers and names ArrayList need to be the same length and the corresponding data in them must be put in synchronized.
     * So that the first entry in the numbers ArrayList corresponds to the first entry in the names ArrayList, and so on.
     * Labels also needs to be synchronized, first entry in the numbers and names ArrayList gets the first entry in the labels ArrayList as a label, and so on.
     * @param numbers an ArrayList of values.
     * @param names an ArrayList of categories.
     * @param labels an ArrayList of labels.
     * @return a DefaultCategoryDataset for a bar chart.
     * @see DefaultCategoryDataset
     */
    public static DefaultCategoryDataset createBarDataset(ArrayList<Double> numbers, ArrayList<String> names, ArrayList<String> labels) {
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        if (names.size() != numbers.size()) {
            dataset.addValue(404, "Error", "Not Found");
        } else {
            for (int i = 0; i < names.size(); i++) {
                dataset.setValue(numbers.get(i), names.get(i), labels.get(i));
            }
        }
        return dataset;
    }

    /**
     * This method creates a line chart with only one dataset.
     *
     * It takes a title, a label for the value axis, an ArrayList of numbers (values), a label for the category axis, an ArrayList of names (categories) and an ArrayList of labels as parameters.
     * @param title a title for the line chart.
     * @param valueAxis a label for the value axis.
     * @param numbers an ArrayList of values.
     * @param catAxis a label for the label axis.
     * @param names an ArrayList of categories.
     * @param labels an ArrayList of labels.
     * @return a JFreeChart line chart.
     * @see JFreeChart
     * @see DefaultCategoryDataset
     */
    public static JFreeChart createLineChart(String title, String valueAxis, ArrayList<Double> numbers, String catAxis, ArrayList<String> names, ArrayList<String> labels) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        if (names.size() != numbers.size()) {
            dataset.addValue(404, "Error", "Not Found");
        } else {
            for (int i = 0; i < names.size(); i++) {
                dataset.addValue(numbers.get(i), names.get(i), labels.get(i));
            }
        }

        JFreeChart lineChart = ChartFactory.createLineChart(title, catAxis, valueAxis, dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        // STYLE LINE CHART
        lineChart.setBackgroundPaint(bg);

        return lineChart;
    }

    /**
     * This method creates a pie chart with one dataset.
     *
     * It takes a title, an ArrayList of names and an ArrayList of numbers (values).
     *
     * The numbers and names ArrayList need to be the same length and the corresponding data in them must be put in synchronized.
     * So that the first entry in the numbers ArrayList corresponds to the first entry in the names ArrayList, and so on.
     * @param title the title of the pie chart.
     * @param names an ArrayList of names.
     * @param numbers an ArrayList of numbers.
     * @return a JFreeChart pie chart.
     * @see JFreeChart
     * @see DefaultPieDataset
     * @see PiePlot
     * @see ChartFactory
     * @see PieSectionLabelGenerator
     * @see StandardPieSectionLabelGenerator
     */
    public static JFreeChart createPieChart(String title, ArrayList<String> names, ArrayList<Double> numbers) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        if (names.size() != numbers.size()) {
            dataset.setValue("VALUES FOR PIE CHART INCORRECT", new Double(20));
        } else {
            for (int i = 0; i < names.size(); i++) {
                dataset.setValue(names.get(i), numbers.get(i));
            }
        }
        JFreeChart pieChart = ChartFactory.createPieChart(title, dataset);

        // STYLE PIE CHART
        PiePlot plot = (PiePlot) pieChart.getPlot();
        PieSectionLabelGenerator gen = new StandardPieSectionLabelGenerator(
                "{0}: {1} ({2})", new DecimalFormat("0"), new DecimalFormat("0%"));
        plot.setLabelGenerator(gen);
        plot.setLabelBackgroundPaint(bg);
        plot.setBackgroundPaint(bg);
        plot.setOutlinePaint(null);

        return pieChart;
    }
}

