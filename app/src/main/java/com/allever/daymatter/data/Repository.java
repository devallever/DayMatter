package com.allever.daymatter.data;

import android.content.Context;
import android.util.Log;

import com.zf.daymatter.R;
import com.allever.daymatter.bean.ItemSlidMenuSort;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Allever on 18/5/26.
 */

public class Repository implements DataSource {

    private static final String TAG = "Repository";

    private Repository(){}

    public static DataSource getIns() {
        return Holder.INSTANCE;
    }

    private static class Holder{
        private static Repository INSTANCE = new Repository();
    }

    @Override
    public String getEventTitle(int id) {
        return null;
    }

    @Override
    public void addDefaultSortData(Context context) {
        Log.d(TAG, "addDefaultSortData: ");
        Event.Sort sortLife = new Event.Sort();
        sortLife.setId(1);
        sortLife.setName(context.getResources().getString(R.string.sort_life));
        sortLife.setDefaultSort(true);
        sortLife.save();

        Event.Sort sortWork = new Event.Sort();
        sortWork.setId(2);
        sortWork.setName(context.getResources().getString(R.string.sort_work));
        sortWork.setDefaultSort(true);
        sortWork.save();

        Event.Sort sortMemoryDay = new Event.Sort();
        sortMemoryDay.setId(3);
        sortMemoryDay.setName(context.getResources().getString(R.string.sort_memory_day));
        sortMemoryDay.setDefaultSort(true);
        sortMemoryDay.save();

    }

    @Override
    public List<ItemSlidMenuSort> getSlidMenuSortData(Context context) {
        List<ItemSlidMenuSort> list = new ArrayList<>();

        //第一项为全部
        ItemSlidMenuSort firstItemSlidMenuSort = new ItemSlidMenuSort();
        firstItemSlidMenuSort.setCount(DataSupport.findAll(Event.class).size());
        firstItemSlidMenuSort.setName(context.getString(R.string.all));
        firstItemSlidMenuSort.setId(0);
        list.add(firstItemSlidMenuSort);

        //查询分类数
        List<Event.Sort> sortList = DataSupport.findAll(Event.Sort.class);
        Log.d(TAG, "getSlidMenuSortData: sort size = " + sortList.size());

        for (Event.Sort sort: sortList){
            ItemSlidMenuSort itemSlidMenuSort = new ItemSlidMenuSort();
            itemSlidMenuSort.setId(sort.getId());
            itemSlidMenuSort.setName(sort.getName());
            Log.d(TAG, "getSlidMenuSortData: id = " + sort.getId());

            //查询该id的事件数
            List<Event> eventList = DataSupport.where("sortId = " + sort.getId()).find(Event.class);
            if (eventList != null){
                itemSlidMenuSort.setCount(eventList.size());
            }else {
                itemSlidMenuSort.setCount(0);
            }
            list.add(itemSlidMenuSort);
        }

        return list;
    }

    @Override
    public boolean saveEvent(String eventTitle, int year, int month, int day, int weekday, int sortId, boolean isTop, int repeatType, boolean isEnd, int endYear, int endMonth, int endDay, int endWeekday) {
        Event event = new Event();
        event.setTitle(eventTitle);
        event.setYear(year);
        event.setMonth(month);
        event.setDay(day);
        event.setWeekDay(weekday);
        event.setSortId(sortId);
        event.setTop(isTop);
        event.setRepeatType(repeatType);
        event.setEndSwitch(isEnd);
        event.setEndYear(endYear);
        event.setEndMonth(endMonth);
        event.setEndDay(endDay);
        event.setEndWeekday(endWeekday);
        event.setLastUpdateTime(System.currentTimeMillis());
        return event.save();
    }

    @Override
    public boolean updateEvent(int eventId, String eventTitle, int year, int month, int day, int weekday, int sortId, boolean isTop, int repeatType, boolean isEnd, int endYear, int endMonth, int endDay, int endWeekday) {
        Event event = DataSupport.find(Event.class, eventId);
        if (event == null){
            return false;
        }

        event.setTitle(eventTitle);
        event.setYear(year);
        event.setMonth(month);
        event.setDay(day);
        event.setWeekDay(weekday);
        event.setSortId(sortId);
        event.setTop(isTop);
        event.setRepeatType(repeatType);
        event.setEndSwitch(isEnd);
        event.setEndYear(endYear);
        event.setEndMonth(endMonth);
        event.setEndDay(endDay);
        event.setEndWeekday(endWeekday);
        event.setLastUpdateTime(System.currentTimeMillis());

        return event.saveOrUpdate("id = " + eventId);
    }

    @Override
    public List<Event> getSortEventList(int sortId) {
        List<Event> list = DataSupport.where("sortId = " + sortId).find(Event.class);
        return list;
    }

    @Override
    public void getSortEventList(final int sortId, final DataListener<List<Event>> dataListener) {
        final List<Event> list = new ArrayList<>();
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                List<Event> eventList = DataSupport.where("sortId = " + sortId).find(Event.class);
                list.addAll(eventList);
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onError(Throwable e) {
                        dataListener.onFail(e.toString());
                    }

                    @Override
                    public void onNext(Object o) {
                    }

                    @Override
                    public void onComplete() {
                        dataListener.onSuccess(list);
                    }
                });
    }

    @Override
    public List<Event> getAllEventList() {
        List<Event> list = DataSupport.findAll(Event.class);
        return list;
    }

    @Override
    public void getAllEventList(final DataListener<List<Event>> dataListener) {
        final List<Event> list = new ArrayList<>();
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                List<Event> eventList = DataSupport.findAll(Event.class);
                list.addAll(eventList);
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onError(Throwable e) {
                        dataListener.onFail(e.toString());
                    }

                    @Override
                    public void onNext(Object o) {
                    }

                    @Override
                    public void onComplete() {
                        dataListener.onSuccess(list);
                    }
                });
    }

    @Override
    public String getSortName(int sortId) {
        String sortName = "";
        Event.Sort sort = DataSupport.find(Event.Sort.class, sortId);
        if (sort != null){
            sortName = sort.getName();
        }
        return sortName;
    }

    @Override
    public Event getEvent(int eventId) {
        Event event = DataSupport.find(Event.class, eventId);
        return event;
    }

    @Override
    public boolean deleteEvent(int eventId) {
        Event event = DataSupport.find(Event.class, eventId);
        if (event == null){
            return true;
        }
        event.delete();
        return true;
    }

    @Override
    public void addDefaultConfig() {
        Log.d(TAG, "addDefaultConfig: ");
        Config config = new Config();

        config.setId(1);

        config.setCurrentDayRemind(1);
        config.setCurrentRemindHour(9);
        config.setCurrentRemindMin(0);

        config.setBeforeDayRemind(1);
        config.setBeforeRemindHour(9);
        config.setBeforeRemindMin(0);

        config.save();
    }

    @Override
    public Config getRemindConfigData() {
        Config config = DataSupport.find(Config.class, 1);
        if (config == null){
            addDefaultConfig();
        }
        config = DataSupport.find(Config.class, 1);
        return config;
    }

    @Override
    public void updateCurrentRemindSwitch(boolean value) {
        Log.d(TAG, "updateCurrentRemindSwitch: " + value);
        Config config = DataSupport.find(Config.class, 1);
        if (value){
            config.setCurrentDayRemind(1);
        }else {
            config.setCurrentDayRemind(0);
        }

        boolean result = config.saveOrUpdate("id = " + config.getId());
        Log.d(TAG, "updateCurrentRemindSwitch: " + result);
    }

    @Override
    public void updateBeforeRemindSwitch(boolean value) {
        Log.d(TAG, "updateBeforeRemindSwitch: " + value);
        Config config = DataSupport.find(Config.class, 1);
        if (value){
            config.setBeforeDayRemind(1);
        }else {
            config.setBeforeDayRemind(0);
        }
        boolean result = config.saveOrUpdate("id = " + config.getId());
        Log.d(TAG, "updateCurrentRemindSwitch: " + result);
    }

    @Override
    public void updateCurrentRemindTime(int hour, int min) {
        Config config = DataSupport.find(Config.class, 1);
        config.setCurrentRemindHour(hour);
        config.setCurrentRemindMin(min);
        boolean result = config.saveOrUpdate("id = " + config.getId());
        Log.d(TAG, "updateCurrentRemindSwitch: " + result);
    }

    @Override
    public void updateBeforeRemindTiem(int hour, int min) {
        Config config = DataSupport.find(Config.class, 1);
        config.setBeforeRemindHour(hour);
        config.setBeforeRemindMin(min);
        boolean result = config.saveOrUpdate("id = " + config.getId());
        Log.d(TAG, "updateCurrentRemindSwitch: " + result);
    }

    @Override
    public List<Event> getEventListByDate(int year, int month, int day) {
        List<Event> eventList = DataSupport.where("year = ? and month = ? and day = ?",
                String.valueOf(year),
                String.valueOf(month),
                String.valueOf(day)
        ).find(Event.class);
        return eventList;
    }

    @Override
    public void getEventListByDate(final int year, final int month, final int day, final DataListener<List<Event>> dataListener) {
        final List<Event> list = new ArrayList<>();
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                List<Event> eventList = DataSupport.where("year = ? and month = ? and day = ?",
                        String.valueOf(year),
                        String.valueOf(month),
                        String.valueOf(day)
                ).find(Event.class);
                list.addAll(eventList);
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onError(Throwable e) {
                        dataListener.onFail(e.toString());
                    }

                    @Override
                    public void onNext(Object o) {
                    }

                    @Override
                    public void onComplete() {
                        dataListener.onSuccess(list);
                    }
                });
    }

    @Override
    public void updateEvent(Event event) {
        event.saveOrUpdate("id = " + event.getId());
    }

    @Override
    public void saveEvent(Event event) {
        event.save();
    }

    @Override
    public void getSlidMenuSortData(final Context context, final DataListener<List<ItemSlidMenuSort>> dataListener) {
        if (context == null || dataListener == null){
            return;
        }

        //异步调用
        final List<ItemSlidMenuSort> list = new ArrayList<>();

        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                //第一项为全部
                ItemSlidMenuSort firstItemSlidMenuSort = new ItemSlidMenuSort();
                firstItemSlidMenuSort.setCount(DataSupport.findAll(Event.class).size());
                firstItemSlidMenuSort.setName(context.getString(R.string.all));
                firstItemSlidMenuSort.setId(0);
                list.add(firstItemSlidMenuSort);

                //查询分类数
                List<Event.Sort> sortList = DataSupport.findAll(Event.Sort.class);
                Log.d(TAG, "getSlidMenuSortData: sort size = " + sortList.size());

                for (Event.Sort sort: sortList){
                    ItemSlidMenuSort itemSlidMenuSort = new ItemSlidMenuSort();
                    itemSlidMenuSort.setId(sort.getId());
                    itemSlidMenuSort.setName(sort.getName());
                    Log.d(TAG, "getSlidMenuSortData: id = " + sort.getId());

                    //查询该id的事件数
                    List<Event> eventList = DataSupport.where("sortId = " + sort.getId()).find(Event.class);
                    if (eventList != null){
                        itemSlidMenuSort.setCount(eventList.size());
                    }else {
                        itemSlidMenuSort.setCount(0);
                    }
                    list.add(itemSlidMenuSort);
                }

                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onError(Throwable e) {
                        dataListener.onFail(e.toString());
                    }

                    @Override
                    public void onNext(Object o) {
                    }

                    @Override
                    public void onComplete() {
                        dataListener.onSuccess(list);
                    }
                });

    }

    @Override
    public void getSortData(Context context, final DataListener<List<Event.Sort>> dataListener) {
        if (context == null || dataListener == null){
            return;
        }

        //异步调用
        final List<Event.Sort> list = new ArrayList<>();

        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                //查询分类数
                List<Event.Sort> sortList = DataSupport.findAll(Event.Sort.class);
                Log.d(TAG, "getSortData: sort size = " + sortList.size());
                list.addAll(sortList);
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onError(Throwable e) {
                        dataListener.onFail(e.toString());
                    }

                    @Override
                    public void onNext(Object o) {
                    }

                    @Override
                    public void onComplete() {
                        dataListener.onSuccess(list);
                    }
                });
    }

    @Override
    public Event.Sort saveSort(String name) {
        Event.Sort sort = new Event.Sort();
        sort.setDefaultSort(false);
        sort.setName(name);
        sort.save();
        return sort;
    }

    @Override
    public void modifySort(int id, String name) {
        Event.Sort sort = DataSupport.find(Event.Sort.class, id);
        sort.setName(name);
        sort.update(id);
    }

    @Override
    public void deleteSort(int id) {
        DataSupport.delete(Event.Sort.class, id);
    }

    /***
     *
    Observable.create(new ObservableOnSubscribe<Object>() {
        @Override
        public void subscribe(ObservableEmitter<Object> emitter) throws Exception {

            emitter.onComplete();
        }
    })
            .subscribeOn(Schedulers.io())
            .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe(new Observer<Object>() {
        @Override
        public void onSubscribe(Disposable d) {
        }

        @Override
        public void onError(Throwable e) {
            dataListener.onFail(e.toString());
        }

        @Override
        public void onNext(Object o) {
        }

        @Override
        public void onComplete() {
            dataListener.onSuccess(list);
        }
    });
    */
}
