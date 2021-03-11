package cn.passwored.data.context;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qchtkj.common.core.util.BeanConvertUtil;
import com.qchtkj.common.core.util.IdWorker;
import com.qchtkj.common.core.util.StringUtil;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Project：eparty
 * Description：数据上下文
 * Date：2021/2/18 20:47
 * Author pandong
 */
public class DataContext {

    private static String idPrefix = "100";

    private final static Logger logger = LoggerFactory.getLogger(DataContext.class);

    /**
     * 根据mapperId进行列表查询操作
     *
     * @param mapperId 功能号
     * @return List<Map>
     */
    public static List<Map> selectList(String mapperId) {
        return selectList(mapperId, new HashMap(1));
    }

    public static List<Map> selectList(String mapperId, String params) {
        return selectList(mapperId, parseStringToMap(params));
    }

    public static List<Map> selectList(String mapperId, Map params) {
        return selectListReal(mapperId, params);
    }

    public static List<Map> selectList(String mapperId, List params) {
        return selectListReal(mapperId, params);
    }

    private static List<Map> selectListReal(String mapperId, Object params) {
        SqlSessionTemplate sqlSessionTemplate = getSqlSessionTemplate();
        sqlLogOut(sqlSessionTemplate, mapperId, params);
        if (sqlSessionTemplate == null) {
            return null;
        }
        if (params == null) {
            return sqlSessionTemplate.selectList(mapperId);
        } else {
            return sqlSessionTemplate.selectList(mapperId, params);
        }
    }

    public static <T> List<T> selectList(String mapperId, Class<T> clazz) {
        return selectList(mapperId, new HashMap(1), clazz);
    }

    public static <T> List<T> selectList(String mapperId, String params, Class<T> clazz) {
        return selectList(mapperId, parseStringToMap(params), clazz);
    }

    public static <T> List<T> selectList(String mapperId, Map params, Class<T> clazz) {
        return selectListReal(mapperId, params, clazz);
    }

    public static <T> List<T> selectList(String mapperId, List params, Class<T> clazz) {
        return selectListReal(mapperId, params, clazz);
    }

    private static <T> List<T> selectListReal(String mapperId, Object params, Class<T> clazz) {
        List<Map> listOld = selectListReal(mapperId, params);
        if (listOld == null) {
            return null;
        }
        List<T> listNew = new ArrayList<>(listOld.size());
        for (Map map : listOld) {
            if (map == null) {
                continue;
            }
            T obj = BeanConvertUtil.convertMapToBean(map, clazz);
            listNew.add(obj);
        }
        return listNew;
    }


    /**
     * 根据mapperId进行分页列表查询操作
     *
     * @param mapperId 功能号
     * @param page     当前页数
     * @param limit    每页行数
     * @return PageInfo
     */
    public static PageInfo selectListByPage(String mapperId, int page, int limit) {
        return selectListByPage(mapperId, new HashMap(1), page, limit);
    }

    public static PageInfo selectListByPage(String mapperId, String params, int page, int limit) {
        return selectListByPage(mapperId, parseStringToMap(params), page, limit);
    }

    public static PageInfo selectListByPage(String mapperId, Map params, int page, int limit) {
        AtomicReference<String> orderBy = new AtomicReference<>("");
        params.forEach((k, v) -> {
            String order = v instanceof String ? v.toString() : "";
            if ("orderBy".equals(k) && StringUtil.isNotBlank(order)) {
                orderBy.set(order);
            }
        });
        return selectListByPageReal(mapperId, params, page, limit, orderBy.get());
    }

    public static PageInfo selectListByPage(String mapperId, List params, int page, int limit) {
        return selectListByPageReal(mapperId, params, page, limit, null);
    }

    public static PageInfo selectListByPageReal(String mapperId, Object params, int page, int limit, String orderBy) {
        SqlSessionTemplate sqlSessionTemplate = getSqlSessionTemplate();
        if (sqlSessionTemplate == null) {
            return null;
        }
        Page<Object> pageInfo = PageHelper.startPage(page, limit);
        if (StringUtil.isNotBlank(orderBy)) {
            pageInfo.setOrderBy(orderBy);
        }
        sqlLogOut(sqlSessionTemplate, mapperId, params);
        if (params == null) {
            return new PageInfo<>(sqlSessionTemplate.selectList(mapperId));
        } else {
            return new PageInfo<>(sqlSessionTemplate.selectList(mapperId, params));
        }
    }

    public static <T> List<T> getListByPageInfo(PageInfo pageInfo, Class<T> clazz) {
        if (pageInfo == null) {
            return null;
        }
        List listOld = pageInfo.getList();
        List<T> listNew = new ArrayList<>();
        for (Object obj : listOld) {
            Map map = (Map) obj;
            T result = BeanConvertUtil.convertMapToBean(map, clazz);
            listNew.add(result);
        }
        return listNew;
    }

    /**
     * 根据mapperId进行单项查询操作
     *
     * @param mapperId 功能号
     * @return Map
     */
    public static Map selectOne(String mapperId) {
        return selectOne(mapperId, new HashMap(1));
    }

    public static Map selectOne(String mapperId, String params) {
        return selectOne(mapperId, parseStringToMap(params));
    }

    public static Map selectOne(String mapperId, Map params) {
        return selectOneReal(mapperId, params);
    }

    public static Map selectOne(String mapperId, List params) {
        return selectOneReal(mapperId, params);
    }

    private static Map selectOneReal(String mapperId, Object params) {
        SqlSessionTemplate sqlSessionTemplate = getSqlSessionTemplate();
        sqlLogOut(sqlSessionTemplate, mapperId, params);
        if (sqlSessionTemplate == null) {
            return null;
        }
        if (params == null) {
            return sqlSessionTemplate.selectOne(mapperId);
        } else {
            return sqlSessionTemplate.selectOne(mapperId, params);
        }
    }

    public static <T> T selectOne(String mapperId, Class<T> clazz) {
        return selectOne(mapperId, new HashMap(1), clazz);
    }

    public static <T> T selectOne(String mapperId, String params, Class<T> clazz) {
        return selectOne(mapperId, parseStringToMap(params), clazz);
    }

    public static <T> T selectOne(String mapperId, Map params, Class<T> clazz) {
        return selectOneReal(mapperId, params, clazz);
    }

    public static <T> T selectOne(String mapperId, List params, Class<T> clazz) {
        return selectOneReal(mapperId, params, clazz);
    }

    private static <T> T selectOneReal(String mapperId, Object params, Class<T> clazz) {
        Map map = selectOneReal(mapperId, params);
        return BeanConvertUtil.convertMapToBean(map, clazz);
    }

    /**
     * 根据mapperId进行添加操作
     *
     * @param mapperId 功能号
     * @param params   参数
     * @return 影响行数
     */
    public static int insert(String mapperId, Map params) {
        SqlSessionTemplate sqlSessionTemplate = getSqlSessionTemplate();
        if (sqlSessionTemplate == null) {
            return 0;
        }
        sqlLogOut(sqlSessionTemplate, mapperId, params);
        return sqlSessionTemplate.insert(mapperId, params);
    }


    public static int insert(String mapperId, Object params) {
        SqlSessionTemplate sqlSessionTemplate = getSqlSessionTemplate();
        if (sqlSessionTemplate == null) {
            return 0;
        }
        sqlLogOut(sqlSessionTemplate, mapperId, params);
        return sqlSessionTemplate.insert(mapperId, params);
    }

    public static int insert(String mapperId, String params) {
        return insert(mapperId, parseStringToMap(params));
    }

    /**
     * 根据mapperId进行更新操作
     *
     * @param mapperId 功能号
     * @param params   参数
     * @return 影响行数
     */
    public static int update(String mapperId, Map params) {
        SqlSessionTemplate sqlSessionTemplate = getSqlSessionTemplate();
        if (sqlSessionTemplate == null) {
            return 0;
        }
        sqlLogOut(sqlSessionTemplate, mapperId, params);
        return sqlSessionTemplate.update(mapperId, params);
    }

    public static int update(String mapperId, String params) {
        return update(mapperId, parseStringToMap(params));
    }

    /**
     * 根据mapperId进行删除操作
     *
     * @param mapperId 功能号
     * @param params   参数
     * @return 影响行数
     */
    public static int delete(String mapperId, Map params) {
        SqlSessionTemplate sqlSessionTemplate = getSqlSessionTemplate();
        if (sqlSessionTemplate == null) {
            return 0;
        }
        sqlLogOut(sqlSessionTemplate, mapperId, params);
        return sqlSessionTemplate.delete(mapperId, params);
    }

    public static int delete(String mapperId, String params) {
        return delete(mapperId, parseStringToMap(params));
    }

    public static SqlSessionTemplate getSqlSessionTemplate() {
        return BaseContext.getBean(SqlSessionTemplate.class);
    }

    private static Map parseStringToMap(String params) {
        Map<String, Object> paramsMap = new HashMap<>(16);
        if (params == null || params.length() == 0) {
            return paramsMap;
        }
        String[] paramList = params.split("&");
        for (String param : paramList) {
            paramsMap.put(param.split("=")[0], param.split("=").length == 1 ? "" : param.split("=")[1]);
        }
        return paramsMap;
    }

    private static void sqlLogOut(SqlSessionTemplate sqlSessionTemplate, String mapperId, Object params) {
        Configuration configuration = sqlSessionTemplate.getConfiguration();
        MappedStatement mappedStatement = configuration.getMappedStatement(mapperId);
        BoundSql boundSql = mappedStatement.getBoundSql(params);
        logger.info(mapperId + ",执行的sql为:\r\n        " + boundSql.getSql());
    }

    public static void setIdPrefix(String prefix) {
        idPrefix = prefix;
    }

    /**
     * 获取随机编号
     *
     * @return
     */
    public static String getSequenceId() {
        Long nextId = IdWorker.getInstance().nextId();
        return idPrefix + nextId;
    }

    public static String getSequenceId(String prefix) {
        String nextId = getSequenceId();
        return prefix + nextId;
    }
}
