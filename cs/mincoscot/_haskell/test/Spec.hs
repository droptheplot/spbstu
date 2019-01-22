module Main where

import Test.QuickCheck

main :: IO ()
main = do
 quickCheck ((minCosCot 2.0) == 0.0)
