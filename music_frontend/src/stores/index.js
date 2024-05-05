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

export const useBlobStore2 = defineStore('blobMap2', () => {
  const imageURLMap = new Map()
  function getBlobURL(name){
    return imageURLMap.get(name)
  }
  function createAndSetBlobURL(name, data){
    const url = window.URL.createObjectURL(data);
    imageURLMap.set(name, url)
    return url
  }
  function deleteBlobURL(name){
    let url = imageURLMap.get(name)
    imageURLMap.delete(name)
    window.URL.revokeObjectURL(url)
  }
  return { getBlobURL, createAndSetBlobURL, deleteBlobURL }
})
