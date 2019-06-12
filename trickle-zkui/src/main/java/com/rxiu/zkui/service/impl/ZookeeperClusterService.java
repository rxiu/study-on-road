package com.rxiu.zkui.service.impl;

import com.google.common.base.Strings;
import com.rxiu.zkui.core.exception.BasicException;
import com.rxiu.zkui.core.exception.ExceptionResult;
import com.rxiu.zkui.domain.ZkCluster;
import com.rxiu.zkui.repository.BaseRepository;
import com.rxiu.zkui.repository.ZookeeperClusterRepository;
import com.rxiu.zkui.service.IZookeeperClusterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shenyuhang
 * @date 2019/6/10
 **/
@Service
public class ZookeeperClusterService extends AbstractBaseService<ZkCluster> implements IZookeeperClusterService {

    @Autowired
    ZookeeperClusterRepository repository;

    @PersistenceContext //注入的是实体管理器,执行持久化操作
    EntityManager entityManager;

    @Override
    public boolean add(ZkCluster cluster) {
        if (repository.findByHostList(cluster.getHostList()) != null) {
            throw new BasicException(ExceptionResult.ENTITY_EXISTS, "hostList");
        }
        return repository.save(cluster).getId() != null;
    }

    @Override
    public boolean save(ZkCluster cluster) {
        if (cluster.getId() == null) {
            throw new BasicException(ExceptionResult.ENTITY_NOT_EXISTS, "集群记录");
        }
        ZkCluster c = repository.findByHostList(cluster.getHostList());
        if (c != null && c.getHostList().equals(cluster.getHostList()) && c.getId() != cluster.getId()) {
            throw new BasicException(ExceptionResult.ENTITY_EXISTS, "hostList");
        }
        return repository.save(cluster).getId() != null;
    }

/*    @Override
    @Modifying
    @Query("delete from Zkculster z where z.id in ?1")
    public void deleteByIds(List<Long> ids) {

    }*/

    @Override
    protected Specification<ZkCluster> createSpecification(ZkCluster cluster) {
        //查询条件构造
        return (Specification<ZkCluster>) (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (!Strings.isNullOrEmpty(cluster.getCode())) {
                predicates.add(cb.like(root.get("code").as(String.class), "%" + cluster.getCode() + "%"));
            }

            if (!Strings.isNullOrEmpty(cluster.getHostList())) {
                predicates.add(cb.like(root.get("hostList").as(String.class), "%" + cluster.getHostList() + "%"));
            }

            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };

    }

    @Override
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    @Override
    protected BaseRepository<ZkCluster> getRepository() {
        return this.repository;
    }
}
