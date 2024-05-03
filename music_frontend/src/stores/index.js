import {defineStore} from 'pinia'

export const useBlobStore = defineStore('blobMap', () => {
  const blobURLMap = new Map()
  function getBlobURL(name){
    return blobURLMap.get(name)
  }
  function createAndSetBlobURL(name, data){
    const url = window.URL.createObjectURL(data);
    blobURLMap.set(name, url)
    return url
  }
  function deleteBlobURL(name){
    let url = blobURLMap.get(name)
    blobURLMap.delete(name)
    window.URL.revokeObjectURL(url)
  }
  return { getBlobURL, createAndSetBlobURL, deleteBlobURL }
})
