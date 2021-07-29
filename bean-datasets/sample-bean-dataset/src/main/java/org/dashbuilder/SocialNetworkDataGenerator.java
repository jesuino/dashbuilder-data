package org.dashbuilder;

import java.util.Map;

import org.dashbuilder.dataset.ColumnType;
import org.dashbuilder.dataset.DataSet;
import org.dashbuilder.dataset.DataSetFactory;
import org.dashbuilder.dataset.DataSetGenerator;

public class SocialNetworkDataGenerator implements DataSetGenerator {

    public DataSet buildDataSet(Map<String, String> params) {
        return DataSetFactory.newDataSetBuilder()
                             .column("Name", ColumnType.LABEL)
                             .column("Company", ColumnType.LABEL)
                             .column("Country", ColumnType.LABEL)
                             .column("Launched", ColumnType.NUMBER)
                             .column("ActiveUsers", ColumnType.NUMBER)
                             .row("Facebook", "Facebook", "United States", "2004", "2797")
                             .row("YouTube", "Alphabet", "United States", "2005", "2291")
                             .row("WhatsApp", "Facebook", "United States", "2009", "2000")
                             .row("Messenger", "Facebook", "United States", "2011", "1300")
                             .row("Instagram", "Facebook", "United States", "2010", "1287")
                             .row("WeChat", "Tencent", "China", "2011", "1225")
                             .row("TikTok", "Bytedance", "China", "2016", "732")
                             .row("Douyin", "Bytedance", "China", "2016", "600")
                             .row("QQ", "Tencent", "China", "1999", "595")
                             .row("Telegram", "Telegram", "United Arab Emirates", "2013", "550")
                             .row("Snapchat", "Snap", "United States", "2011", "528")
                             .row("Weibo", "Sina", "China", "2009", "521")
                             .row("Qzone", "Tencent", "China", "2005", "517")
                             .row("Kuaishou", "Kuaishou", "China", "2011", "481")
                             .row("Pinterest", "Pinterest", "United States", "2009", "459")
                             .row("Reddit", "Reddit", "United States", "2005", "430")
                             .row("Twitter", "Twitter", "United States", "2006", "396")
                             .row("Quora", "Quora", "United States", "2009", "300")
                             .row("Skype", "Microsoft", "Luxembourg", "2003", "300")
                             .row("Tieba", "Baidu", "China", "2003", "300")
                             .row("Viber", "Rakuten", "Luxembourg", "2010", "260")
                             .row("LinkedIn", "Microsoft", "United States", "2003", "250")
                             .row("imo", "PageBites", "United States", "2012", "200")
                             .row("Line", "Naver", "Japan", "2011", "169")
                             .row("PicsArt", "PicsArt", "United States", "2011", "150")
                             .row("Likee", "Bigo Live", "Singapore", "2017", "150")
                             .row("Discord", "Discord", "United States", "2015", "140")
                             .buildDataSet();

    }

}