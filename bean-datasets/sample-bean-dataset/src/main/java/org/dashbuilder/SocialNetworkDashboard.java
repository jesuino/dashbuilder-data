package org.dashbuilder;

import static org.dashbuilder.dsl.factory.page.PageFactory.columnBuilder;
import static org.dashbuilder.dsl.factory.page.PageFactory.row;

import java.nio.file.Paths;
import java.util.List;

import org.dashbuilder.dataset.def.DataSetDefFactory;
import org.dashbuilder.dataset.group.AggregateFunctionType;
import org.dashbuilder.dataset.sort.SortOrder;
import org.dashbuilder.displayer.DisplayerSettingsFactory;
import org.dashbuilder.dsl.factory.dashboard.DashboardFactory;
import org.dashbuilder.dsl.factory.page.PageFactory;
import org.dashbuilder.dsl.model.Dashboard;
import org.dashbuilder.dsl.serialization.DashboardExporter;
import org.dashbuilder.dsl.serialization.DashboardExporter.ExportType;

public class SocialNetworkDashboard {

    public static void main(String[] args) throws Exception {
        var basePath = args.length > 0 ? args[0] : ".";
        var dashboard = buildDashboard();
        var outPath = Paths.get(basePath).resolve("social-networks-dashboard.zip");
        DashboardExporter.get()
                         .export(dashboard,
                                 outPath.toString(),
                                 ExportType.ZIP);
    }

    static Dashboard buildDashboard() {
        var socialNetworksDef = DataSetDefFactory.newBeanDataSetDef()
                                                 .generatorClass("org.dashbuilder.SocialNetworkDataGenerator")
                                                 .uuid("socialNetworks")
                                                 .buildDef();

        socialNetworksDef.setAllColumnsEnabled(true);
        // [Name TEXT, Company TEXT, Country TEXT, Launched NUMBER, ActiveUsers NUMBER]
        var byCountryBarChart = DisplayerSettingsFactory.newBarChartSettings()
                                                        .width(10000)
                                                        .resizableOn(10000, 500)
                                                        .margins(0, 0, 100, 0)
                                                        .dataset(socialNetworksDef.getUUID())
                                                        .filterOn(false, true, false)
                                                        .group("Country")
                                                        .column("Country")
                                                        .column("Name", AggregateFunctionType.COUNT, "Total")
                                                        .format("Total", "#")
                                                        .sort("Total", SortOrder.DESCENDING)
                                                        .buildSettings();

        var byActiveUsersChart = DisplayerSettingsFactory.newBarChartSettings()
                                                         .width(10000)
                                                         .resizableOn(10000, 500)
                                                         .margins(0, 0, 100, 0)
                                                         .dataset(socialNetworksDef.getUUID())
                                                         .filterOn(false, true, true)
                                                         .group("Name")
                                                         .column("Name")
                                                         .column("ActiveUsers", AggregateFunctionType.MAX, "Total")
                                                         .format("Total", "#")
                                                         .buildSettings();
        var table = DisplayerSettingsFactory.newTableSettings()
                                            .dataset(socialNetworksDef.getUUID())
                                            .resizable(true)
                                            .tableColumnPickerEnabled(false)
                                            .tablePageSize(100)
                                            .column("Name")
                                            .column("Country")
                                            .column("Company")
                                            .column("Launched")
                                            .format("Launched", "#")
                                            .column("ActiveUsers")
                                            .format("Active Users", "#")
                                            .sort("ActiveUsers", SortOrder.DESCENDING)
                                            .filterOn(false, false, true)
                                            .buildSettings();

        var mainPage = PageFactory.page("Social Networks",
                                        row("<h3>Social Networks Stats</h3><hr />"),
                                        row(
                                            columnBuilder().rows(
                                                                 row("<h4>By Country</h4>"),
                                                                 row(byCountryBarChart))
                                                           .property("width", "50%")
                                                           .build(),
                                            columnBuilder().rows(
                                                                 row("<h4>By Active Users</h4>"),
                                                                 row(byActiveUsersChart))
                                                           .property("width", "50%")
                                                           .build()),

                                        row(table));
        return DashboardFactory.dashboard(List.of(mainPage),
                                          List.of(socialNetworksDef));
    }

}
