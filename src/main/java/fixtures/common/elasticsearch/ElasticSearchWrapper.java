package fixtures.common.elasticsearch;

import java.util.List;
import java.util.Random;

import cucumber.table.DataTable;
import fixtures.common.RowToObjectDataSource;
import fixtures.common.rows.RowsToObject;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.node.NodeBuilder;

public class ElasticSearchWrapper implements RowToObjectDataSource {
    private static final int BIG_ID_INTERVAL = 1000000;

    private static final boolean HOSTING_NO_DATA = true;

    private Client client;

    private DataTable dataTable;

    private String index;

    private String type;

    private static Random random = new Random();

    public ElasticSearchWrapper(DataTable dataTable, String index, String type) {
        this.client = NodeBuilder.nodeBuilder().client(HOSTING_NO_DATA).build().client();
        this.dataTable = dataTable;
        this.index = index;
        this.type = type;
    }

    public BulkResponse persistAndIndex() {
        final BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
        RowsToObject<XContentBuilder> rowsToObject = new RowsToObject<XContentBuilder>(dataTable, this, Document.class);
        final List<XContentBuilder> documents = rowsToObject.executeInRows();
        for (XContentBuilder document : documents) {
            final IndexRequestBuilder indexRequestBuilder = indexRow(client, document);
            bulkRequestBuilder.add(indexRequestBuilder);
        }
        return bulkRequestBuilder.execute().actionGet();
    }

    private IndexRequestBuilder indexRow(final Client client,
            XContentBuilder xContentBuilder) {

        return client.prepareIndex(index, type, random.nextInt(BIG_ID_INTERVAL) + "").setSource(xContentBuilder);
    }
}
