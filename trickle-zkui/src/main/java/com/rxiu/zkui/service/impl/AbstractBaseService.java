package com.rxiu.zkui.service.impl;

import com.google.common.collect.Lists;
import com.rxiu.zkui.common.PageList;
import com.rxiu.zkui.repository.BaseRepository;
import com.rxiu.zkui.service.IBaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * @author shenyuhang
 * @date 2019/6/12
 **/
public abstract class AbstractBaseService<T> implements IBaseService<T> {

    protected abstract BaseRepository<T> getRepository();

    @Override
    public PageList<T> getByPage(Integer page, Integer size, T t) {
        Pageable pageable = PageRequest.of(page, size);     //分页信息
        Specification<T> spec = createSpecification(t);     //查询条件
        Page<T> records = getRepository().findAll(spec, pageable);
        return new PageList(records.getNumber(), records.getTotalPages(), records.getSize(), records.getContent());
    }

    protected abstract Specification<T> createSpecification(T t);

    @Override
    public T getById(Long id) {
        return getRepository().findById(id).get();
    }

    @Override
    public void delete(Long id) {
        getRepository().deleteById(id);
    }

    @Override
    public void delete(T t) {
        getRepository().delete(t);
    }

    @Override
    @Transactional
    public boolean deleteByIds(List<Long> ids) {
        boolean flag = false;
        int index = 1;
        String tableName = ((Class<T>) (((ParameterizedType) this.getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0])).getName();
        StringBuilder sql = new StringBuilder("delete from " + tableName + " u WHERE u.id in (");
        if (ids != null && ids.size() != 0) {
            for (Long id : ids) {
                sql.append("?").append(index).append(",");
                index++;
            }
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(")");
        Query query = getEntityManager().createQuery(sql.toString());
        for (int x = 0; x < ids.size(); x++) {
            query.setParameter(x + 1, ids.get(x));
        }
        int i = query.executeUpdate();
        if (i != 0) {
            flag = true;
        }
        getEntityManager().close();
        return flag;
    }

    protected abstract EntityManager getEntityManager();
}
