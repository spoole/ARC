package com.google.gwt.chrome.storage;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Items under the "sync" storage area are synced using Chrome Sync.
 * 
 * @author jarrod
 * 
 */
public class SyncStorageArea implements StorageArea {
	/**
	 * The maximum number of items that can be stored in sync storage.
	 * <p>
	 * Updates that would cause this limit to be exceeded will fail immediately
	 * and set chrome.runtime.lastError.
	 * </p>
	 * <p>
	 * Value: 512
	 * </p>
	 */
	public static final int MAX_ITEMS = 512;
	/**
	 * The maximum number of set, remove, or clear operations that can be
	 * performed each minute, sustained over 10 minutes.
	 * <p>
	 * Updates that would cause this limit to be exceeded fail immediately and
	 * set chrome.runtime.lastError.
	 * </p>
	 * <p>
	 * Value: 10
	 * </p>
	 */
	public static final int MAX_SUSTAINED_WRITE_OPERATIONS_PER_MINUTE = 10;
	/**
	 * The maximum number of set, remove, or clear operations that can be
	 * performed each hour.
	 * <p>
	 * Updates that would cause this limit to be exceeded fail immediately and
	 * set chrome.runtime.lastError.
	 * </p>
	 * <p>
	 * Value: 1,000
	 * </p>
	 */
	public static final int MAX_WRITE_OPERATIONS_PER_HOUR = 1000;
	/**
	 * The maximum total amount (in bytes) of data that can be stored in sync
	 * storage, as measured by the JSON stringification of every value plus
	 * every key's length.
	 * <p>
	 * Updates that would cause this limit to be exceeded fail immediately and
	 * set chrome.runtime.lastError.
	 * </p>
	 * <p>
	 * Value: 102,400 B
	 * </p>
	 */
	public static final int QUOTA_BYTES = 102400;
	/**
	 * The maximum size (in bytes) of each individual item in sync storage, as
	 * measured by the JSON stringification of its value plus its key length.
	 * <p>
	 * Updates containing items larger than this limit will fail immediately and
	 * set chrome.runtime.lastError.
	 * </p>
	 * <p>
	 * Value: 4,096 B
	 * </p>
	 */
	public static final int QUOTA_BYTES_PER_ITEM = 4096;
	
	protected SyncStorageArea(){}
	
	@Override
	public void getBytesInUse(String key, StorageUseCallback callback) {
		getBytesInUse(new String[]{key}, callback);
	}
	
	@Override
	public final native void getBytesInUse(String[] keys, StorageUseCallback callback) /*-{
		$wnd.chrome.storage.sync.getBytesInUse(keys, $entry(function(bytesInUse){
			if(!bytesInUse && bytesInUse != 0){
				callback.@com.google.gwt.chrome.storage.StorageArea.StorageUseCallback::onError(Ljava/lang/String;)($wnd.chrome.runtime.lastError);
				return;
			}
			callback.@com.google.gwt.chrome.storage.StorageArea.StorageUseCallback::onCalculate(D)(bytesInUse);
		}));
	}-*/;
	
	@Override
	public final native void clear(StorageSimpleCallback callback) /*-{
		$wnd.chrome.storage.sync.clear($entry(function(){
			callback.@com.google.gwt.chrome.storage.StorageArea.StorageSimpleCallback::onDone()();
		}));
	}-*/;

	@Override
	public final native void set(JavaScriptObject data, StorageSimpleCallback callback) /*-{
		$wnd.chrome.storage.sync.set(data, $entry(function(){
			callback.@com.google.gwt.chrome.storage.StorageArea.StorageSimpleCallback::onDone()();
		}));
	}-*/;

	@Override
	public void remove(String key, StorageSimpleCallback callback) {
		remove(new String[]{key}, callback);
	}

	@Override
	public final native void remove(String[] keys, StorageSimpleCallback callback) /*-{
		$wnd.chrome.storage.sync.remove(keys, $entry(function(){
			callback.@com.google.gwt.chrome.storage.StorageArea.StorageSimpleCallback::onDone()();
		}));
	}-*/;

	@Override
	public final native void get(JavaScriptObject keysWithDefaults,
			StorageItemsCallback callback) /*-{
		$wnd.chrome.storage.sync.get(keysWithDefaults, $entry(function(items){
			if(!items){
				console.warn('$wnd.chrome.storage.sync.get resulted with no values where some values should be available.', chrome.runtime.lastError);
				callback.@com.google.gwt.chrome.storage.StorageArea.StorageItemsCallback::onError(Ljava/lang/String;)(chrome.runtime.lastError);
				return;
			}
			callback.@com.google.gwt.chrome.storage.StorageArea.StorageItemsCallback::onResult(Lcom/google/gwt/core/client/JavaScriptObject;)(items);
		}));
	}-*/;

	@Override
	public final native void get(StorageItemsCallback callback) /*-{
		$wnd.chrome.storage.sync.get(null, $entry(function(items){
			if(chrome.runtime.lastError){
				callback.@com.google.gwt.chrome.storage.StorageArea.StorageItemsCallback::onError(Ljava/lang/String;)(chrome.runtime.lastError);
				return;
			}
			callback.@com.google.gwt.chrome.storage.StorageArea.StorageItemsCallback::onResult(Lcom/google/gwt/core/client/JavaScriptObject;)(items);
		}));
	}-*/;

}
