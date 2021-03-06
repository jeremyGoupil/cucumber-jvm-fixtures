package fixtures.common.datatable;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Constructor;
import java.util.List;

import com.google.common.collect.Lists;
import cucumber.api.DataTable;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

public class DatatableUtilsTest {
    @Test
    public void testConstructor() throws Exception {
        Constructor constructor = DatatableUtils.class.getDeclaredConstructor();
        // on vérifie que le contructeur est volontairement inacessible
        assertThat(constructor.isAccessible(), CoreMatchers.is(false));

        // pas utile au final, mais la couverture du constructeur est prise en compte
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_getDatatable_headers_is_null() throws Exception {

        List<String> headers = null;
        List<List<String>> values = Lists.<List<String>>newArrayList(Lists.<String>newArrayList("0", "0.st", "877"),
                Lists.<String>newArrayList("1", "fs.sys", "878D"));

        DatatableUtils.getDatatable(headers, values);
    }

    @Test
    public void test_getDatatable_headers_is_empty() throws Exception {

        List<String> headers = Lists.newArrayList();
        List<List<String>> values = Lists.<List<String>>newArrayList(Lists.<String>newArrayList("0", "0.st", "877"),
                Lists.<String>newArrayList("1", "fs.sys", "878D"));

        DataTable datatable = DatatableUtils.getDatatable(headers, values);
        //
        //
        List<List<String>> expected = Lists.<List<String>>newArrayList(Lists.<String>newArrayList("0", "0.st", "877"),
                Lists.<String>newArrayList("1", "fs.sys", "878D"));
        assertThat(datatable.raw(), is(expected));
    }

    // le header n'est pas récupéré !!
    @Test
    public void test_getDatatable_nominal_case() throws Exception {

        List<String> headers = Lists.newArrayList("ID", "FILE", "REF");
        List<List<String>> values = Lists.<List<String>>newArrayList(Lists.<String>newArrayList("0", "0.st", "877"),
                Lists.<String>newArrayList("1", "fs.sys", "878D"));

        DataTable datatable = DatatableUtils.getDatatable(headers, values);
        //
        //
        List<List<String>> expected = Lists.<List<String>>newArrayList(Lists.<String>newArrayList("0", "0.st", "877"),
                Lists.<String>newArrayList("1", "fs.sys", "878D"));
        assertThat(datatable.raw(), is(expected));
    }

    @Test
    public void test_getDatatable_nominal_case_values_empty() throws Exception {

        List<String> headers = Lists.newArrayList("ID", "FILE", "REF");
        List<List<String>> values = Lists.newArrayList();

        DataTable datatable = DatatableUtils.getDatatable(headers, values);
        //
        //
        List<List<String>> expected = Lists.newArrayList();
        assertThat(datatable.raw(), is(expected));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_getDatatable_nominal_case_values_null() throws Exception {

        List<String> headers = Lists.newArrayList("ID", "FILE", "REF");
        List<List<String>> values = null;

        DatatableUtils.getDatatable(headers, values);
    }
}
