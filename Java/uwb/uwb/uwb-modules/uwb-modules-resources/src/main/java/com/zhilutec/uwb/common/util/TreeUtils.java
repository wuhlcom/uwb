package com.zhilutec.uwb.common.util;


import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者 杨柳
 * 创建时间 2017-07-05 16:54
 */
@Component
public class TreeUtils {

    /**
     * @param list               树数据
     * @param root               根节点
     * @param keyFieldName       关联属性
     * @param parentKeyFieldName 关联父属性
     * @param subFieldName       子节点数据
     * @param <T>                根节点
     */
    public static <T> void createTree(List<T> list, T root, String keyFieldName, String parentKeyFieldName, String subFieldName) {
        Field keyField = ReflectUtils.getField(keyFieldName, root);
        Field parentKeyField = ReflectUtils.getField(parentKeyFieldName, root);
        Field subField = ReflectUtils.getField(subFieldName, root);
        find(list, root, keyField, parentKeyField, subField);
    }

    /**
     * 根据父节点的关联值 查找
     */
    public static <T, E> List<E> getKeys(List<T> list, T root, String keyFieldName, String parentKeyFieldName) {
        Field keyField = ReflectUtils.getField(keyFieldName, root);
        Field parentKeyField = ReflectUtils.getField(parentKeyFieldName, root);
        List<E> keys = new ArrayList<>();
        E value = ReflectUtils.getValueByGetMethod(keyField, root);
        keys.add(value);
        findKeys(list, keys, root, keyField, parentKeyField);
        return keys;
    }

    private static <T> void find(List<T> list, T parent, Field keyField, Field parentKeyField, Field subField) {
        List<T> subs = getSubs(list, parent, keyField, parentKeyField);

        if (subs != null) {
            ReflectUtils.setValueByField(subField, parent, subs);
            for (T sub : subs) {
                //递归找子节点
                find(list, sub, keyField, parentKeyField, subField);
            }
        }
    }

    private static <T, E> List<E> findKeys(List<T> list, List<E> keys, T parent, Field keyField, Field parentKeyField) {
        List<T> subs = getSubs(list, parent, keyField, parentKeyField);

        List<E> subKeys = getSubKeys(list, parent, keyField, parentKeyField);
        if (subs != null) {
            keys.addAll(subKeys);
            for (T sub : subs) {
                //递归找子节点
                findKeys(list, keys, sub, keyField, parentKeyField);
            }
        }
        return keys;
    }

    private static <T> List<T> getSubs(List<T> list, T parent, Field keyField, Field parentKeyField) {
        List<T> subs = null;
        for (T t : list) {
            Object keyFieldValue = ReflectUtils.getValueByField(keyField, parent);
            Object parentFieldValue = ReflectUtils.getValueByField(parentKeyField, t);
            if (keyFieldValue.equals(parentFieldValue)) {
                if (subs == null) {
                    subs = new ArrayList<>();
                }
                subs.add(t);
            }
        }
        return subs;
    }

    private static <T, E> List<E> getSubKeys(List<T> list, T parent, Field keyField, Field parentKeyField) {
        List<E> subs = null;
        for (T t : list) {
            Object keyFieldValue = ReflectUtils.getValueByField(keyField, parent); //父节点key
            Object parentFieldValue = ReflectUtils.getValueByField(parentKeyField, t); //根结点关联的key
            if (keyFieldValue.equals(parentFieldValue)) { //关联字段相等
                if (subs == null) {
                    subs = new ArrayList<>();
                }
                //取子节点key
                Object key = ReflectUtils.getValueByField(keyField, t);
                subs.add((E) key);
            }
        }
        return subs;
    }


    // 例子
    // public static void main(String[] args) {
        // TestTree testTree = new TestTree();
        // testTree.setId("1001");
        // testTree.setName("潍坊市");
        // testTree.setPid("1000");
        //
        // TestTree testTree1 = new TestTree();
        // testTree1.setId("1002");
        // testTree1.setName("青岛市");
        // testTree1.setPid("1000");
        //
        // TestTree testTree2 = new TestTree();
        // testTree2.setId("1001001");
        // testTree2.setName("高新区");
        // testTree2.setPid("1001");
        //
        // TestTree testTree3 = new TestTree();
        // testTree3.setId("1002001");
        // testTree3.setName("四方区");
        // testTree3.setPid("1002");
        //
        // TestTree testTree4 = new TestTree();
        // testTree4.setId("1000");
        // testTree4.setName("山东省");
        // testTree4.setPid("0");
        //
        // TestTree testTree5 = new TestTree();
        // testTree5.setId("1001001001");
        // testTree5.setName("清池街办");
        // testTree5.setPid("1001001");

        // List<TestTree> testTreeList = new ArrayList<>();
        // testTreeList.add(testTree);
        // testTreeList.add(testTree1);
        // testTreeList.add(testTree2);
        // testTreeList.add(testTree3);
        // testTreeList.add(testTree4);
        // testTreeList.add(testTree5);

        // TreeUtils.createTree(testTreeList, testTree4, "id", "pid", "testTrees");
        // System.out.println(testTree4); //通过上边的createTree方法，根节点，即testTree4就是最全的最后的树结构。

        // List<Department> departments = TreeUtils.getDepartments();
        // System.out.println(departments);

    }


// }

// class TestTree {
//     private String id;
//     private String name;
//     private String pid;
//     private List<TestTree> testTrees;
//
//     public List<TestTree> getTestTrees() {
//         return testTrees;
//     }
//
//     public void setTestTrees(List<TestTree> testTrees) {
//         this.testTrees = testTrees;
//     }
//
//     public String getId() {
//         return id;
//     }
//
//     public void setId(String id) {
//         this.id = id;
//     }
//
//     public String getName() {
//         return name;
//     }
//
//     public void setName(String name) {
//         this.name = name;
//     }
//
//     public String getPid() {
//         return pid;
//     }
//
//     public void setPid(String pid) {
//         this.pid = pid;
//     }
//
//     // @Override
//     // public String toString() {
//     //     return "TestTree{" +
//     //             "id='" + id + '\'' +
//     //             ", name='" + name + '\'' +
//     //             ", pid='" + pid + '\'' +
//     //             ", testTrees=" + testTrees +
//     //             '}';
//     // }
//
//     @Override
//     public String toString() {
//         JSONObject jsonObject = new JSONObject();
//         jsonObject.put("id", this.id);
//         jsonObject.put("name", this.name);
//         jsonObject.put("pid", this.pid);
//         jsonObject.put("testTrees", this.testTrees);
//         return jsonObject.toJSONString();
//     }
// }