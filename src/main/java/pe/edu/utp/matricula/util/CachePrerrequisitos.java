package pe.edu.utp.matricula.util;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import org.springframework.stereotype.Component;
import pe.edu.utp.matricula.entity.Prerrequisito;
import pe.edu.utp.matricula.repository.PrerrequisitoRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Component
public class CachePrerrequisitos {

    private final LoadingCache<Long, ImmutableList<Prerrequisito>> cache;

    public CachePrerrequisitos(PrerrequisitoRepository prerrequisitoRepository) {
        this.cache = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build(new CacheLoader<>() {
                    @Override
                    public ImmutableList<Prerrequisito> load(Long cursoId) {
                        List<Prerrequisito> list = prerrequisitoRepository.findByCursoId(cursoId);
                        return ImmutableList.copyOf(list);
                    }
                });
    }

    public ImmutableList<Prerrequisito> getPrerrequisitos(Long cursoId) {
        try {
            return cache.get(cursoId);
        } catch (ExecutionException e) {
            return ImmutableList.of();
        }
    }
}
