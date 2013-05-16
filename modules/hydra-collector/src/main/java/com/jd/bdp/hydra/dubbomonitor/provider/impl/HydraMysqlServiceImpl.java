package com.jd.bdp.hydra.dubbomonitor.provider.impl;

import com.jd.bdp.hydra.Span;
import com.jd.bdp.hydra.dubbomonitor.HydraService;
import com.jd.bdp.hydra.store.inter.InsertService;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HydraMysqlServiceImpl implements HydraService {

    private ArrayBlockingQueue<List<Span>> queue = new ArrayBlockingQueue<List<Span>>(1024);
    private ExecutorService executors = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());


    public HydraMysqlServiceImpl(){
        for(int i = 0;i < 3 ;i++){
            executors.execute(new InsertTask());
        }
    }

    class InsertTask implements Runnable{
        @Override
        public void run() {
            while(true){
                try {
                    List<Span> span = queue.take();
                    if(span != null){
                        for(Span s : span){
                            insertService.addSpan(s);
                            insertService.addTrace(s);
                            insertService.addAnnotation(s);
                        }
                    }
                } catch (InterruptedException e) {
                    //ig
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }
    }

    @Override
    public boolean push(List<Span> span) throws IOException {
        return queue.add(span);
    }

    private InsertService insertService;
    public void setInsertService(InsertService insertService) {
        this.insertService = insertService;
    }


}