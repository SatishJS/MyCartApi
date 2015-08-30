package mykart

import groovy.sql.Sql
import com.google.gson.Gson;

class productController {
    def dataSource;

    def list() {
        println "list"
        Sql sql = new Sql(dataSource)
        Gson gson = new Gson();
        def res = sql.rows("select p.id, p.name, c.name as categoryname from products p join categories c on p.category_id=c.id")
        response.setHeader("Access-Control-Allow-Origin", "*")
        render gson.toJson(res)
    }

    def getListByCategoryId() {
        def id = params.id
        Sql sql = new Sql(dataSource)
        Gson gson = new Gson();
        def res = sql.rows("select * from products where category_id = " + id)
        response.setHeader("Access-Control-Allow-Origin", "*")
        render gson.toJson(res)
    }

    def getListByName() {
        def name = params.id
        Sql sql = new Sql(dataSource)
        Gson gson = new Gson();
        def res = sql.rows("select * from products where name like '%" + name + "%'")
        response.setHeader("Access-Control-Allow-Origin", "*")
        render gson.toJson(res)
    }

    def getListWithMinPrice() {
        Sql sql = new Sql(dataSource)
        Gson gson = new Gson();
        def query = """\
        select b.*, v.name as vendor_name
from
(
SELECT p.*, pm.vendor_id, pm.vendor_product_id, a.minprice, ifnull(pm.vendor_product_url,'') as vendor_product_url
FROM products p
\t\tjoin product_mappings pm
\ton p.id=pm.product_id
        join (
\t\t\t\tselect min(price) as minprice, product_id
\t\t\t\tfrom product_mappings
\t\t\t\tgroup by product_id
\t\t\t) as a
    on a.product_id=pm.product_id and pm.price = minprice
 )  as b
\t\tjoin vendors v on b.vendor_id=v.id
        """
        def res = sql.rows(query)
        response.setHeader("Access-Control-Allow-Origin", "*")
        render gson.toJson(res)
    }

    def getMinPriceById() {
        Sql sql = new Sql(dataSource)
        def id = params.id;
        Gson gson = new Gson();
        def query = """\
        select b.*, v.name as vendor_name
from
(
SELECT p.*, pm.vendor_id, pm.vendor_product_id, a.minprice, ifnull(pm.vendor_product_url,'') as vendor_product_url
FROM products p
\t\tjoin product_mappings pm
\ton p.id=pm.product_id
        join (
\t\t\t\tselect min(price) as minprice, product_id
\t\t\t\tfrom product_mappings
\t\t\t\tgroup by product_id
\t\t\t) as a
    on a.product_id=pm.product_id and pm.price = minprice
 )  as b
\t\tjoin vendors v on b.vendor_id=v.id
where b.id = """
        query += id
        def res = sql.rows(query)
        response.setHeader("Access-Control-Allow-Origin", "*")
        render gson.toJson(res)
    }

    def getMinPriceByName() {
        Sql sql = new Sql(dataSource)
        def name = params.id;
        Gson gson = new Gson();
        def query = """\
        select b.*, v.name as vendor_name
from
(
SELECT p.*, pm.vendor_id, pm.vendor_product_id, a.minprice, ifnull(pm.vendor_product_url,'') as vendor_product_url
FROM products p
\t\tjoin product_mappings pm
\ton p.id=pm.product_id
        join (
\t\t\t\tselect min(price) as minprice, product_id
\t\t\t\tfrom product_mappings
\t\t\t\tgroup by product_id
\t\t\t) as a
    on a.product_id=pm.product_id and pm.price = minprice
 )  as b
\t\tjoin vendors v on b.vendor_id=v.id
where b.name like '%"""
        query += name + "%'"
        def res = sql.rows(query)
        response.setHeader("Access-Control-Allow-Origin", "*")
        render gson.toJson(res)
    }
}
